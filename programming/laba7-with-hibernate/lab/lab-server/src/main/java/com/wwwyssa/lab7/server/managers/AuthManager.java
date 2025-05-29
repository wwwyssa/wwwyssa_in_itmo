package com.wwwyssa.lab7.server.managers;

import com.wwwyssa.lab7.common.util.executions.AnswerString;
import com.wwwyssa.lab7.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab7.server.Server;
import com.wwwyssa.lab7.server.dao.UserDAO;
import com.wwwyssa.lab7.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

public class AuthManager {
    private String pepper;
    private final SessionFactory sessionFactory;
    private static volatile AuthManager instance;

    private AuthManager() {
        String url = "jdbc:postgresql://localhost:5432/laba7";
        String user = "postgres";
        String password = "123";
        this.sessionFactory = HibernateUtil.getSessionFactory(url, user, password);
    }

    public static AuthManager getInstance() {
        if (instance == null) {
            synchronized (AuthManager.class) {
                if (instance == null) {
                    instance = new AuthManager();
                }
            }
        }
        return instance;
    }

    public int registerUser(String login, String password) throws SQLException {
        Server.logger.info("Создание нового пользователя " + login);

        var salt = PasswordManager.getSalt();
        var passwordHash = PasswordManager.getHash(password, salt);

        UserDAO dao = new UserDAO();
        dao.setName(login);
        dao.setPassword(passwordHash);
        dao.setSalt(salt);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.persist(dao);
                transaction.commit();

                var newId = dao.getId();
                Server.logger.info("Пользователь успешно создан, id#" + newId);
                return newId;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public int authenticateUser(String login, String password) throws SQLException {
        Server.logger.info("Аутентификация пользователя " + login);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

                var query = session.createQuery("SELECT u FROM users u WHERE u.name = :name", UserDAO.class);
                query.setParameter("name", login);

                List<UserDAO> result = query.getResultList();

                if (result.isEmpty()) {
                    Server.logger.info("Неправильный пароль для пользователя " + login);
                    return -1; // Пользователь не найден
                }

                var user = result.get(0);
                transaction.commit();

                var id = user.getId();
                var salt = user.getSalt();
                var expectedHashedPassword = user.getPassword();

                var actualHashedPassword = PasswordManager.getHash(password, salt);
                if (expectedHashedPassword.equals(actualHashedPassword)) {
                    Server.logger.info("Пользователь " + login + " аутентифицирован c id#" + id);
                    return id; // Успешная аутентификация
                }

                Server.logger.info(
                        "Неправильный пароль для пользователя " + login +
                                ". Ожидалось '" + expectedHashedPassword + "', получено '" + actualHashedPassword + "'"
                );
                return -1;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }
}