package soya.framework.nezha.pipeline.resource;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;
import soya.framework.nezha.FunctionNode;
import soya.framework.nezha.pipeline.server.PipelineDeployService;
import soya.framework.nezha.pipeline.server.PipelineLogService;
import soya.framework.nezha.pipeline.server.PipelineTriggerEvent;
import soya.framework.nezha.pipeline.server.PipelineServer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Component
@Path("/console")
@Api(value = "Settler Service")
public class ServerResource {
    @POST
    @Path("/ast")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON})
    public Response ast(String expression) {
        Gson gson = new Gson();
        FunctionNode[] functions = FunctionNode.toFunctions(expression);
        return Response.status(200).entity(gson.toJson(functions)).build();
    }

    @GET
    @Path("/deployments")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deployments() {
        PipelineDeployService service = PipelineServer.getInstance().getService(PipelineDeployService.class);
        return Response.status(200).entity(service.getDeployments()).build();
    }

    @GET
    @Path("/deployment/{pipeline}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deployment(@PathParam("pipeline") String pipeline) {
        PipelineDeployService service = PipelineServer.getInstance().getService(PipelineDeployService.class);
        return Response.status(200).entity(service.getDeployments()).build();
    }

    @POST
    @Path("/pipeline/{pipeline}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response trigger(@PathParam("pipeline") String pipeline) {
        PipelineServer.getInstance().publish(new PipelineTriggerEvent(pipeline));
        return Response.status(200).build();
    }

    @GET
    @Path("/log/{pipeline}")
    @Produces({MediaType.TEXT_PLAIN})
    public Response log(@PathParam("pipeline") String pipeline, @QueryParam("offset") int offset,
                        @QueryParam("limit") int limit, @QueryParam("reverse") boolean reverse, @QueryParam("indexed") boolean indexed) {
        PipelineLogService service = PipelineServer.getInstance().getService(PipelineLogService.class);
        try {
            return Response.status(200).entity(service.read(pipeline, offset, limit, reverse, indexed)).build();
        } catch (IOException e) {
            return Response.status(400).build();
        }

    }
}