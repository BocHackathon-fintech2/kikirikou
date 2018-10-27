package io.kikirikou.apps.pipelines.managers.impl.pipelinemodules;

import io.kikirikou.apps.pipelines.managers.decl.FilterManager;
import io.kikirikou.apps.pipelines.other.JsonUtils;
import io.kikirikou.apps.pipelines.other.PipelineProcessor;
import org.apache.tapestry5.json.JSONObject;

import java.util.stream.Stream;

public class Filter implements PipelineProcessor {
    private final FilterManager filterManager;

    public Filter(FilterManager filterManager) {
        this.filterManager = filterManager;
    }


    @Override
    public Stream<JSONObject> process(Stream<JSONObject> stream, JSONObject config) {
        String column = config.getString("column");
        String op = config.getString("op");
        Object val = config.get("value");

        return stream.filter(jsonObject -> filterManager.fitler(op, val, JsonUtils.flatten(jsonObject).get(column)));
    }
}
