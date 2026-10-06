package service.notification;

import jakarta.annotation.Resource;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionSynchronizationRegistry;
import jakarta.websocket.Session;
import jakarta.websocket.CloseReason;
import java.io.IOException;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Lock(LockType.READ)
public class ChangeNotifier {
    private final Set<Session> sessions = ConcurrentHashMap.newKeySet();

    @Resource
    private TransactionSynchronizationRegistry transactions;

    public void connect(Session session, long expiresAt) {
        session.getUserProperties().put("expiresAt", expiresAt);
        sessions.add(session);
    }

    public void disconnect(Session session) {
        sessions.remove(session);
    }

    public void changed() {
        transactions.registerInterposedSynchronization(new Synchronization() {
            @Override
            public void beforeCompletion() {
            }

            @Override
            public void afterCompletion(int status) {
                if (status != Status.STATUS_COMMITTED) {
                    return;
                }
                for (Session session : sessions) {
                    try {
                        long expiresAt = (Long) session.getUserProperties().get("expiresAt");
                        if (expiresAt <= Instant.now().getEpochSecond()) {
                            sessions.remove(session);
                            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Authentication expired"));
                            continue;
                        }
                        if (session.isOpen()) {
                            session.getAsyncRemote().sendText("changed", result -> {
                                if (!result.isOK()) {
                                    sessions.remove(session);
                                }
                            });
                        } else {
                            sessions.remove(session);
                        }
                    } catch (IOException | RuntimeException exception) {
                        sessions.remove(session);
                    }
                }
            }
        });
    }
}
