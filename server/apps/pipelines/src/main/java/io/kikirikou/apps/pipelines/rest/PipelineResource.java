package io.kikirikou.apps.pipelines.rest;

import io.kikirikou.apps.pipelines.managers.decl.PipelineExecutor;
import org.apache.tapestry5.json.JSONObject;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pipeline")
@Produces(MediaType.APPLICATION_JSON)
public class PipelineResource {
	private final PipelineExecutor pipelineExecutor;

	public PipelineResource(PipelineExecutor pipelineExecutor) {
		this.pipelineExecutor = pipelineExecutor;
	}

	@POST
	public Response get(JSONObject payload) {
		this.pipelineExecutor.execute(payload);
		return Response.accepted().build();
	}
}
