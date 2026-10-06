package web;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.Dependent;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnMessage;
import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import service.notification.ChangeNotifier;
import service.auth.JwtService;
import service.exception.UnauthorizedException;
import java.io.IOException;

@ServerEndpoint("/updates")
@Dependent
public class UpdatesEndpoint {
    @EJB
    private ChangeNotifier changeNotifier;
    @EJB
    private JwtService jwtService;

    @OnOpen
    public void open(Session session) {
        session.setMaxIdleTimeout(10_000);
        session.setMaxTextMessageBufferSize(8192);
    }

    @OnMessage
    public void authenticate(String token, Session session) throws IOException {
        if (session.getUserProperties().containsKey("expiresAt")) {
            return;
        }
        try {
            JwtService.Identity identity = jwtService.verify(token);
            session.setMaxIdleTimeout(0);
            changeNotifier.connect(session, identity.expiresAt());
            session.getAsyncRemote().sendText("ready");
        } catch (UnauthorizedException exception) {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Authentication required"));
        }
    }

    @OnClose
    public void close(Session session) {
        changeNotifier.disconnect(session);
    }
}
