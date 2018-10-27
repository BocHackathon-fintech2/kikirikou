package io.kikirikou.apps.pipelines.managers.impl.pipelinemodules;

import io.kikirikou.apps.pipelines.other.JsonUtils;
import io.kikirikou.apps.pipelines.other.PipelineProcessor;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StringTable implements PipelineProcessor {
    private final TypeCoercer typeCoercer;

    public StringTable(TypeCoercer typeCoercer) {
        this.typeCoercer = typeCoercer;
    }

    @Override
    public Stream<JSONObject> process(Stream<JSONObject> stream, JSONObject config) {
        String[] columns = config.getJSONArray("columns").toList().stream().map(o -> (String)o).toArray(String[]::new);
        List<List<String>> collect = stream.map(JsonUtils::flatten).map(map -> Arrays.stream(columns).
                map(s -> typeCoercer.coerce(map.get(s), String.class)).
                collect(Collectors.toList())).
                collect(Collectors.toList());


        List<String> firstRow = collect.iterator().next();
        int[] ints = IntStream.range(0, firstRow.size()).
                map(i -> collect.stream().
                        mapToInt(strings -> strings.get(i).length()).max().
                        orElseThrow(IllegalStateException::new)).toArray();

        String value = collect.stream().
                map(strings -> IntStream.range(0, strings.size()).
                        mapToObj(i -> String.format("%" + ints[i] + "s", strings.get(i))).
                        collect(Collectors.joining(" | "))).
                collect(Collectors.joining("\n"));

        return Stream.of(new JSONObject("value",value));
    }
}
