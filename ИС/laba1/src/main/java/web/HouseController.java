package web;

import dto.HouseData;
import dto.HouseRequest;
import jakarta.ejb.EJB;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import service.house.HouseService;

@Path("/houses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HouseController {
    @EJB
    private HouseService houseService;

    @GET
    public Response list(@BeanParam PageParameters page, @QueryParam("name") String name) {
        return ApiResponses.ok(houseService.list(page.toRequest(), name));
    }

    @POST
    public Response create(HouseRequest request, @Context UriInfo uriInfo) {
        HouseData house = houseService.create(request);
        return ApiResponses.created(house,
                uriInfo.getAbsolutePathBuilder().path(house.id.toString()).build());
    }

    @GET
    @Path("/{id}")
    public Response find(@PathParam("id") long id) {
        return ApiResponses.ok(houseService.find(id));
    }
}
