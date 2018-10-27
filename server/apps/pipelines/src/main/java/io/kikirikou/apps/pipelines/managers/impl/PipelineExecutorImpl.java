package io.kikirikou.apps.pipelines.managers.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;

import io.kikirikou.apps.pipelines.managers.decl.PipelineExecutor;
import io.kikirikou.apps.pipelines.managers.decl.PipelineFactory;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PipelineExecutorImpl implements PipelineExecutor {

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private final PipelineFactory pipelineFactory;
	private final ExecutorService executorService;
	private final OkHttpClient httpClient;
	private final Logger logger;

	public PipelineExecutorImpl(PipelineFactory pipelineFactory,
			ExecutorService executor,
			OkHttpClient httpClient,
			Logger logger) {
		this.pipelineFactory = pipelineFactory;
		this.executorService = executor;
		this.httpClient = httpClient;
		this.logger = logger;
	}

	private Collection<JSONObject> toCollection(JSONArray data) {
		return data.toList().stream().map(o -> (JSONObject)o).collect(Collectors.toList());
	}

	private void post(JSONArray arg0, String url) throws IOException {
		RequestBody body = RequestBody.create(JSON, arg0.toCompactString());
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		Response response = httpClient.newCall(request).execute();
		response.body().close();
	}

	@Override
	public CompletableFuture<JSONArray> execute(JSONObject payload) {
		String callback = payload.has("callback") ? payload.getString("callback") :  null;

		CompletableFuture<JSONArray> fut = CompletableFuture.supplyAsync(()-> {
			JSONArray data = payload.getJSONArray("data");
			final JSONArray config = payload.getJSONArray("config");

			JSONArray from = JSONArray.from(pipelineFactory.build(toCollection(data).stream(),
					toCollection(config)).
					collect(Collectors.toList()));

			return from;
		}, executorService);

		if (callback != null) {
			
			fut = fut.whenComplete(new BiConsumer<JSONArray,Throwable>() {
				@Override
				public void accept(JSONArray arg0, Throwable arg1) {
					if (arg1 != null) {
						logger.error("{}", payload, arg1);
						return;
					}

					try {
						PipelineExecutorImpl.this.post(arg0, callback);
					} catch (IOException e) {
						logger.error("Error when callback", e);
					}
				}

			});
		}
		return fut;
	}
}
