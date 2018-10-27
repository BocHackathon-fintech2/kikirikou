package io.kikirikou.apps.pipelines.managers.impl.pipelinemodules;

import io.kikirikou.apps.pipelines.other.JsonUtils;
import io.kikirikou.apps.pipelines.other.PipelineProcessor;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringTable implements PipelineProcessor {
    private final TypeCoercer typeCoercer;

    public StringTable(TypeCoercer typeCoercer) {
        this.typeCoercer = typeCoercer;
    }

    @Override
    public Stream<JSONObject> process(Stream<JSONObject> stream, JSONObject config) {
        String[] columns = config.getJSONArray("columns").toList().stream().map(o -> (String)o).toArray(String[]::new);
        String collect = stream.map(JsonUtils::flatten).map(map -> Arrays.stream(columns).
                map(s -> typeCoercer.coerce(map.get(s), String.class)).
                collect(Collectors.joining("|"))).collect(Collectors.joining("\n"));

        return Stream.of(new JSONObject("value",collect));
    }
}
