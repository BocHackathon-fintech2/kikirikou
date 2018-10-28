package io.kikirikou.apps.backoffice.managers.decl;

import java.io.IOException;

import org.apache.tapestry5.json.JSONObject;

public interface PipelineExecutor {
	int runPipeline(JSONObject payload) throws IOException;
}
