package io.kikirikou.apps.backoffice.managers.impl;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.ws.rs.core.MediaType;

import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;

import io.kikirikou.apps.backoffice.managers.decl.PipelineExecutor;
import io.kikirikou.apps.backoffice.managers.decl.PipelineManager;
import io.kikirikou.apps.backoffice.other.ApplicationSymbolConstants;
import io.kikirikou.modules.persistence.managers.decl.NotificationListenerManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PipelineExecutorImpl implements PipelineExecutor {
	
	private final PipelineManager pipelineManager;
	private final String pipelinesUrl;
	private final OkHttpClient httpClient;
	private final Logger log;
	
	public PipelineExecutorImpl(PipelineManager manager, @Symbol(ApplicationSymbolConstants.PIPELINES_URL) String pipelinesUrl,
			OkHttpClient httpClient, Logger log,
			NotificationListenerManager notificationListenerManager) {
		this.pipelineManager = manager;
		this.pipelinesUrl = pipelinesUrl;
		this.httpClient = httpClient;
		this.log = log;
		
		notificationListenerManager.listen("new_transaction", (s, jsonObject) -> {
            String accountId = jsonObject.getString("account_id");
            JSONObject trans = jsonObject.getJSONObject("transaction");

            this.runTransactionTriggers(accountId, trans);
        });
	}
	
	private void runTransactionTriggers(String accountId, JSONObject transaction) {
		
		JSONObject all = pipelineManager.getAll();
		all.keys().stream().map(new Function<String, JSONObject>() {
		    @Override
		    public JSONObject apply(String s) {
		        return all.getJSONObject(s);
		    }
		}).filter(new Predicate<JSONObject>() {
		    @Override
		    public boolean test(JSONObject jsonObject) {
		        JSONObject trigger = jsonObject.getJSONObject("trigger");
		        if(!trigger.getString("type").equalsIgnoreCase("transaction"))
		            return false;

		        return trigger.getJSONObject("configuration").getString("account").equals(accountId);
		    }
		}).map(new Function<JSONObject, JSONObject>() {
		    @Override
		    public JSONObject apply(JSONObject jsonObject) {
		        return new JSONObject("data",new JSONArray(transaction),"config",jsonObject.getJSONArray("configuration"));
		    }
		}).forEach(new Consumer<JSONObject>() {
		    @Override
		    public void accept(JSONObject jsonObject) {
		        try {
					PipelineExecutorImpl.this.runPipeline(jsonObject);
				} catch (IOException e) {
					log.error("Error running pipeline", e);
				}
		    }
		});
	}
	
	@Override
	public int runPipeline(JSONObject payload) throws IOException {
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse(MediaType.APPLICATION_JSON), payload.toCompactString());

		Request req = new Request.Builder()
				.url(this.pipelinesUrl)
				.post(body).build();
		
		okhttp3.Response res = this.httpClient.newCall(req).execute();
		res.body().close();
		
		return res.code();
	}
}
