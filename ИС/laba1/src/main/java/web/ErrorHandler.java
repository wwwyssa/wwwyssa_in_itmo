package web;

import jakarta.json.bind.JsonbException;
import jakarta.json.stream.JsonParsingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;
import service.exception.BadRequestException;
import service.exception.NotFoundException;
import service.exception.ConflictException;
import service.exception.UnauthorizedException;

@Provider
public class ErrorHandler implements ExceptionMapper<Exception> {
    private static final String INVALID_REQUEST =
            "Invalid JSON or parameter value. Check numeric types and enum values";

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof UnauthorizedException) {
            return Response.fromResponse(ApiResponses.error(401, exception.getMessage()))
                    .header("WWW-Authenticate", "Bearer").build();
        }
        if (exception instanceof ConflictException) {
            return ApiResponses.error(409, exception.getMessage());
        }
        if (exception instanceof BadRequestException) {
            return ApiResponses.error(400, exception.getMessage());
        }
        if (exception instanceof NotFoundException) {
            return ApiResponses.error(404, exception.getMessage());
        }
        for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
            if (cause instanceof JsonbException || cause instanceof JsonParsingException) {
                return ApiResponses.error(400, INVALID_REQUEST);
            }
        }
        if (exception instanceof WebApplicationException webException) {
            Response original = webException.getResponse();
            int status = original.getStatus();
            String message = switch (status) {
                case 400 -> INVALID_REQUEST;
                case 404 -> "Resource not found";
                case 405 -> "Method not allowed";
                case 415 -> "Content-Type must be application/json";
                default -> status >= 500 ? "Ошибка БД" : "HTTP request failed";
            };
            return Response.fromResponse(original).entity(Map.of("message", message))
                    .type(MediaType.APPLICATION_JSON).build();
        }
        return ApiResponses.error(500, "Ошибка БД");
    }
}
