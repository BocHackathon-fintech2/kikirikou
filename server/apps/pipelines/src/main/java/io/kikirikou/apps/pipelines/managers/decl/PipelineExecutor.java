package io.kikirikou.apps.pipelines.managers.decl;

import java.util.concurrent.CompletableFuture;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

public interface PipelineExecutor {

	CompletableFuture<JSONArray> execute(JSONObject payload);

}