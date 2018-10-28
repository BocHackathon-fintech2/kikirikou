package io.kikirikou.apps.pipelines.managers.impl;

import io.kikirikou.apps.pipelines.managers.decl.PipelineFactory;
import io.kikirikou.apps.pipelines.other.PipelineProcessor;
import org.apache.tapestry5.json.JSONObject;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

public class PipelineFactoryImpl implements PipelineFactory {
    private static final JSONObject EMPTY_CONFIG = new JSONObject();
    private final Map<String, PipelineProcessor> configuration;

    public PipelineFactoryImpl(Map<String, PipelineProcessor> configuration) {
        this.configuration = configuration;
    }

    @Override
    public Stream<JSONObject> build(Stream<JSONObject> stream, Collection<JSONObject> config) {
        for(JSONObject conf:config)
            stream = configuration.get(conf.getString("type")).process(stream, conf.has("config") ? conf.getJSONObject("config") : EMPTY_CONFIG);
        return stream;
    }
}
