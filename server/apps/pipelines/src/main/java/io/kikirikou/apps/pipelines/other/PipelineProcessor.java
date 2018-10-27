package io.kikirikou.apps.pipelines.other;

import org.apache.tapestry5.json.JSONObject;

import java.util.stream.Stream;

@FunctionalInterface
public interface PipelineProcessor {
    Stream<JSONObject> process(Stream<JSONObject> stream, JSONObject config);
}
