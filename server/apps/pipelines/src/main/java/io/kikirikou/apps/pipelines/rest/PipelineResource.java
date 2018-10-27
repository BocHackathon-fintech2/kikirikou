package io.kikirikou.apps.pipelines.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/pipeline")
public class PipelineResource {
    @GET
    public Response get() {
        return Response.ok().build();
    }
}
