package io.kikirikou.apps.pipelines.managers.impl.pipelinemodules;

import io.kikirikou.apps.pipelines.enums.Aggregator;
import io.kikirikou.apps.pipelines.other.JsonUtils;
import io.kikirikou.apps.pipelines.other.PipelineProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregate implements PipelineProcessor {

    private final TypeCoercer typeCoercer;

    public Aggregate(TypeCoercer typeCoercer) {
        this.typeCoercer = typeCoercer;
    }


    @Override
    public Stream<JSONObject> process(Stream<JSONObject> stream, JSONObject config) {
        String groupBy = config.has("groupBy") ? config.getString("groupBy") : null;

        String[] columns = config.getJSONArray("columns").toList().stream().map(o -> (String)o).toArray(String[]::new);
        Aggregator aggregator = Aggregator.valueOf(config.getString("execute"));


        if(StringUtils.isNotBlank(groupBy))
            return stream.collect(Collectors.groupingBy(jsonObject -> JsonUtils.flatten(jsonObject).get(groupBy))).
                    entrySet().
                    stream().
                    flatMap((Function<Map.Entry<Object, List<JSONObject>>, Stream<JSONObject>>) entry ->
                            Stream.of(aggregate(entry.getValue().stream(),columns,aggregator).
                                    put("key",entry.getKey())));
        else
            return Stream.of(aggregate(stream,columns,aggregator));
    }

    private JSONObject aggregate(Stream<JSONObject> stream,String[] columns,Aggregator aggregator) {
        Stream<BigDecimal> bigDecimalStream = stream.map(new Function<JSONObject, BigDecimal>() {
            @Override
            public BigDecimal apply(JSONObject jsonObject) {
                Map<String, Object> flatten = JsonUtils.flatten(jsonObject);
                return Arrays.stream(columns).map(new Function<String, BigDecimal>() {
                    @Override
                    public BigDecimal apply(String s) {
                        return typeCoercer.coerce(flatten.get(s), BigDecimal.class);
                    }
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
        });

        return new JSONObject("value",aggregateStream(bigDecimalStream,aggregator));
    }

    private BigDecimal aggregateStream(Stream<BigDecimal> stream,Aggregator aggregator) {
        List<BigDecimal> collect = stream.collect(Collectors.toList());
        switch(aggregator) {
            case SUM:
                return collect.stream().reduce(BigDecimal.ZERO,BigDecimal::add);
            case COUNT:
                return BigDecimal.valueOf(collect.size());
            case MIN:
                return collect.stream().min(BigDecimal::compareTo).
                        orElse(collect.get(0));
            case MAX:
                return stream.max(Comparator.reverseOrder()).
                        orElse(collect.get(0));
            case AVERAGE:
                return collect.stream().reduce(BigDecimal.ZERO,BigDecimal::add).divide(BigDecimal.valueOf(collect.size()), RoundingMode.HALF_EVEN);
            default:
                throw new RuntimeException();
        }
    }
}
