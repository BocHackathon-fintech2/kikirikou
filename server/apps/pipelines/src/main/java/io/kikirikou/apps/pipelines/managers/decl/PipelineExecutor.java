package io.kikirikou.apps.pipelines.managers.decl;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import java.util.concurrent.CompletableFuture;

public interface PipelineExecutor {

	CompletableFuture<JSONArray> execute(JSONObject payload);

}