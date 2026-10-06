package web;

import jakarta.annotation.Priority;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import java.security.Principal;
import service.auth.JwtService;
import service.exception.UnauthorizedException;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {
    @EJB
    private JwtService jwtService;

    @Override
    public void filter(ContainerRequestContext request) {
        String path = request.getUriInfo().getPath().replaceFirst("^/+", "");
        if (path.equals("auth/login") || path.equals("auth/register")) {
            return;
        }
        String authorization = request.getHeaderString("Authorization");
        try {
            if (authorization == null || !authorization.regionMatches(true, 0, "Bearer ", 0, 7)) {
                throw new UnauthorizedException("Войдите в систему");
            }
            JwtService.Identity identity = jwtService.verify(authorization.substring(7).trim());
            boolean secure = request.getSecurityContext().isSecure();
            request.setSecurityContext(new SecurityContext() {
                public Principal getUserPrincipal() { return identity::username; }
                public boolean isUserInRole(String role) { return "user".equals(role); }
                public boolean isSecure() { return secure; }
                public String getAuthenticationScheme() { return "Bearer"; }
            });
        } catch (UnauthorizedException exception) {
            request.abortWith(jakarta.ws.rs.core.Response.fromResponse(ApiResponses.error(401, exception.getMessage()))
                    .header("WWW-Authenticate", "Bearer").build());
        }
    }
}
