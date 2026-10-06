package web;

import dto.AuthRequest;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.auth.AuthService;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {
    @EJB
    private AuthService authService;

    @POST
    @Path("/register")
    public Response register(AuthRequest request) {
        return Response.status(201).entity(authService.register(request)).header("Cache-Control", "no-store").build();
    }

    @POST
    @Path("/login")
    public Response login(AuthRequest request) {
        return Response.ok(authService.login(request)).header("Cache-Control", "no-store").build();
    }
}
