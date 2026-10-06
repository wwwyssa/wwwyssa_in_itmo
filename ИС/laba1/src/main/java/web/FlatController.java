package web;

import dto.FlatData;
import dto.FlatRequest;
import jakarta.ejb.EJB;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import service.flat.FlatService;
import service.flat.FlatSearch;

@Path("/flats")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FlatController {
    @EJB
    private FlatService flatService;

    @GET
    public Response list(@BeanParam PageParameters page, @QueryParam("name") String name,
                         @QueryParam("houseName") String houseName,
                         @QueryParam("numberOfRooms") String numberOfRooms,
                         @QueryParam("timeToMetroOnFoot") String timeToMetroOnFoot) {
        return ApiResponses.ok(flatService.list(new FlatSearch(page.toRequest(), name, houseName,
                QueryNumbers.optionalInt(numberOfRooms, "numberOfRooms"),
                QueryNumbers.optionalFloat(timeToMetroOnFoot, "timeToMetroOnFoot"))));
    }

    @POST
    public Response create(FlatRequest request, @Context UriInfo uriInfo) {
        FlatData flat = flatService.create(request);
        return ApiResponses.created(flat,
                uriInfo.getAbsolutePathBuilder().path(flat.id.toString()).build());
    }

    @GET
    @Path("/{id}")
    public Response find(@PathParam("id") int id) {
        return ApiResponses.ok(flatService.find(id));
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, FlatRequest request) {
        return ApiResponses.ok(flatService.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        return ApiResponses.ok(flatService.delete(id));
    }
}
