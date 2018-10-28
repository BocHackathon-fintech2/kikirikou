package io.kikirikou.apps.backoffice.rest;

import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONObject;

import io.kikirikou.apps.backoffice.managers.decl.PipelineManager;
import io.kikirikou.apps.backoffice.other.ApplicationSymbolConstants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@Path("/pipeline")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PipelineResource {
	
	private final PipelineManager pipelineManager;
	private final OkHttpClient httpClient;
	private final String pipelinesUrl;

	public PipelineResource(@Symbol(ApplicationSymbolConstants.PIPELINES_URL) String pipelinesUrl, PipelineManager pipelineManager, OkHttpClient httpClient) {
		this.pipelineManager = pipelineManager;
		this.httpClient = httpClient;
		this.pipelinesUrl = pipelinesUrl;
	}

	@GET
	public JSONObject getAll() {
		return this.pipelineManager.getAll();
	}
	
	@Path("{id}")
	@PUT
	public Response createOrUpdate(@PathParam("id") String id, JSONObject document) {
		this.pipelineManager.createOrUpdate(id, document);
		
		return Response.ok().build();
	}

	@POST
	public Response create(JSONObject document) {
		String id = UUID.randomUUID().toString();
		this.pipelineManager.createOrUpdate(id, document);
		
		return Response.ok(id).build();
	}
	
	@Path("/run")
	@POST
	public Response execute(JSONObject payload) throws IOException {
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse(MediaType.APPLICATION_JSON), payload.toCompactString());

		Request req = new Request.Builder()
				.url(this.pipelinesUrl)
				.post(body).build();
		
		okhttp3.Response res = this.httpClient.newCall(req).execute();
		res.body().close();
		
		return Response.status(res.code()).build();
	}
}
