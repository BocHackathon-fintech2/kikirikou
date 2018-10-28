package io.kikirikou.apps.backoffice.rest;

import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONObject;

import io.kikirikou.apps.backoffice.managers.decl.PipelineExecutor;
import io.kikirikou.apps.backoffice.managers.decl.PipelineManager;
import io.kikirikou.apps.backoffice.other.ApplicationSymbolConstants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@Path("/pipeline")
@Consumes(MediaType.APPLICATION_JSON)
public class PipelineResource {
	
	private final PipelineManager pipelineManager;
	private final PipelineExecutor pipelineExecutor;

	public PipelineResource(PipelineExecutor pipelineExecutor, PipelineManager pipelineManager) {
		this.pipelineManager = pipelineManager;
		this.pipelineExecutor = pipelineExecutor;
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
		int res = this.pipelineExecutor.runPipeline(payload);
		return Response.status(res).build();
	}
}
