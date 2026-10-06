package web;

import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;

public final class ApiResponses {
    private ApiResponses() {
    }

    public static Response ok(Object result) {
        return Response.ok(result).build();
    }

    public static Response created(Object result, URI location) {
        return Response.created(location).entity(result).build();
    }

    public static Response error(int status, String message) {
        return Response.status(status).entity(Map.of("message", message))
                .type("application/json").build();
    }
}
