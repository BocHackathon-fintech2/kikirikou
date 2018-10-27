package io.kikirikou.apps.pipelines.managers.decl;

import org.apache.tapestry5.json.JSONObject;

import java.util.Collection;
import java.util.stream.Stream;

public interface PipelineFactory {
    Stream<JSONObject> build(Stream<JSONObject> stream, Collection<JSONObject> config);
}
