package web;

import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.flat.FlatOperationsService;

@Path("/flats/operations")
@Produces(MediaType.APPLICATION_JSON)
public class FlatOperationsController {
    @EJB
    private FlatOperationsService flatOperationsService;

    @GET
    @Path("/max-area")
    public Response maxArea() {
        return ApiResponses.ok(flatOperationsService.maxArea());
    }

    @GET
    @Path("/count-by-house")
    public Response countByHouse(@QueryParam("houseId") String houseId) {
        return ApiResponses.ok(flatOperationsService.countByHouse(QueryNumbers.optionalLong(houseId, "houseId")));
    }

    @GET
    @Path("/name-prefix")
    public Response namesStartingWith(@QueryParam("prefix") String prefix) {
        return ApiResponses.ok(flatOperationsService.namesStartingWith(prefix));
    }

    @GET
    @Path("/most-expensive-without-balcony")
    public Response mostExpensiveWithoutBalcony() {
        return ApiResponses.ok(flatOperationsService.mostExpensiveWithoutBalcony());
    }

    @GET
    @Path("/most-expensive-of-three")
    public Response mostExpensiveOfThree(@QueryParam("id1") String id1,
                                         @QueryParam("id2") String id2, @QueryParam("id3") String id3) {
        return ApiResponses.ok(flatOperationsService.mostExpensiveOfThree(
                QueryNumbers.optionalInt(id1, "id1"), QueryNumbers.optionalInt(id2, "id2"), QueryNumbers.optionalInt(id3, "id3")));
    }
}
