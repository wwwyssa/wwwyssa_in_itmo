package repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.User;

@Stateless
public class UserRepository {
    @PersistenceContext(unitName = "flatsPU")
    private EntityManager entityManager;

    public User find(String username) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username).getResultStream().findFirst().orElse(null);
    }

    public boolean create(String username, String passwordHash) {
        // The unique constraint also handles simultaneous registrations.
        return entityManager.createNativeQuery("INSERT INTO app_user (username, password_hash) "
                + "VALUES (?, ?) ON CONFLICT (username) DO NOTHING")
                .setParameter(1, username).setParameter(2, passwordHash).executeUpdate() == 1;
    }
}
