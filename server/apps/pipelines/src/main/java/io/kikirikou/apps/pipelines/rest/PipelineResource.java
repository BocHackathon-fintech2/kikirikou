package io.kikirikou.apps.pipelines.rest;

import io.kikirikou.apps.pipelines.managers.decl.PipelineFactory;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("/pipeline")
@Produces(MediaType.APPLICATION_JSON)
public class PipelineResource {
    private final PipelineFactory pipelineFactory;

    public PipelineResource(PipelineFactory pipelineFactory) {
        this.pipelineFactory = pipelineFactory;
    }

    @POST
    public Response get(JSONObject payload) {
        JSONArray data = payload.getJSONArray("data");
        JSONArray config = payload.getJSONArray("config");

        JSONArray from = JSONArray.from(pipelineFactory.build(toCollection(data).stream(),
                toCollection(config)).
                collect(Collectors.toList()));
        return Response.ok(from).build();
    }

    private Collection<JSONObject> toCollection(JSONArray data) {
        return data.toList().stream().map(o -> (JSONObject)o).collect(Collectors.toList());
    }
}
