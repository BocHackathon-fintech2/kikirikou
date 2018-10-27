package io.kikirikou.apps.pipelines.managers.impl.pipelinemodules;

import io.kikirikou.apps.pipelines.other.PipelineProcessor;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;

import java.util.stream.Stream;

public class Console implements PipelineProcessor {
    private final Logger logger;

    public Console(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Stream<JSONObject> process(Stream<JSONObject> stream, JSONObject config) {
        return stream.peek(jsonObject -> logger.info("{}",jsonObject));
    }
}
