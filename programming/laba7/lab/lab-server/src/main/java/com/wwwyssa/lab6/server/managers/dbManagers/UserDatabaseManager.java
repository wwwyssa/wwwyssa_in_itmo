package com.wwwyssa.lab6.server.managers.dbManagers;



import com.wwwyssa.lab6.common.models.ServerUser;
import com.wwwyssa.lab6.common.util.User;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.server.managers.PasswordManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс для взаимодействий с таблицей пользователей из базы данных.
 */
public class UserDatabaseManager {

    private final DbConnectionManager connectionManager;
    private static volatile UserDatabaseManager instance;


    public UserDatabaseManager(String url, String login, String password) {
        connectionManager = new DbConnectionManager(url, login, password);
    }

    public UserDatabaseManager(DbConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    /**
     * Получает единственный экземпляр класса UserDatabaseManager
     *
     * @return единственный экземпляр класса UserDatabaseManager
     */
    public static UserDatabaseManager getInstance() {
        if (instance == null) {
            synchronized (ProductDatabaseManager.class) {
                if (instance == null) {
                    // "jdbc:postgresql://pg:5432/studs", "s466650", "suzaNzpmt8VQyTrD"
                    //"jdbc:postgresql://localhost:5432/laba7", "postgres", "123"
                    instance = new UserDatabaseManager("jdbc:postgresql://localhost:5432/laba7", "postgres", "123"); // пароль из файла .pgpass
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return connectionManager.getConnection();
    }

    /**
     * Добавляет пользователя в базу данных
     *
     * @param user пользователь, которого добавляем
     * @return int id добавленного пользователя
     */
    public ExecutionResponse addUser(User user) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO USERS(name, password_digest, salt)" +
                        "VALUES (?, ?, ?) RETURNING id");

        statement.setString(1, user.getName());
        String salt = PasswordManager.getSalt();
        statement.setString(2, PasswordManager.getHash(user.getPassword(), salt));
        statement.setString(3, salt);

        ResultSet result = statement.executeQuery();

        connection.close();

        result.next();

        return new ExecutionResponse(true, new AnswerString("Пользователь " + user.getName() + " добавлен в базу данных"));
    }

    /**
     * Получает пользователя по имени
     *
     * @param id id пользователя
     * @return ServerUser полученный пользователь
     */
    public ServerUser getUser(int id) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM users WHERE id = ?");

        statement.setInt(1, id);

        ResultSet result = statement.executeQuery();

        connection.close();

        result.next();

        String name = result.getString("name");
        String password_digest = result.getString("password_digest");
        String salt = result.getString("salt");
        String role = result.getString("role");


        return new ServerUser(id, name, password_digest, salt);
    }


    /**
     * Получает пользователя по имени
     *
     * @param name - имя пользователя
     * @return ServerUser полученный пользователь
     */
    public ServerUser getUser(String name) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM users WHERE name = ?"
        );

        statement.setString(1, name);

        ResultSet result = statement.executeQuery();

        connection.close();

        result.next();

        int id = result.getInt("id");
        String password_digest = result.getString("password_digest");
        String salt = result.getString("salt");


        return new ServerUser(id, name, password_digest, salt);
    }

    /**
     * Получает пользователя по имени и паролю
     *
     * @param name     - имя пользователя
     * @param password - пароль пользователя
     * @return ServerUser полученный пользователь
     */
    public ServerUser getUser(String name, String password) throws SQLException {
        ServerUser user = getUser(name.strip());
        //System.out.println("Пользователь " + user.getName() + " найден " + user.getPasswordDigest());
        String salt = user.getSalt();
        String password_digest = PasswordManager.getHash(password, salt);
       // System.out.println("ПОлученный пароль:  " + password_digest);


        if (password_digest.equals(user.getPasswordDigest())) {
           // System.out.println("Пользователь " + name + " авторизован");
            return user;
        }
        return null;
    }

    /**
     * Получает соль по имени пользователя
     *
     * @param name - имя пользователя
     * @return String - соль пользователя
     */
    public String getUserSalt(String name) throws SQLException {
        ServerUser user = getUser(name);

        return user.getSalt();
    }

    /**
     * Получает id по имени пользователя
     *
     * @param name - имя пользователя
     * @return int - id пользователя
     */
    public int getUserId(String name) throws SQLException {
        ServerUser user = getUser(name);
        return user.getId();
    }



    /**
     * Проверяет, есть ли пользователь с таким именем
     *
     * @param name - имя пользователя, которое проверяем
     * @return - true - есть, false - нет
     */
    public boolean checkUserName(String name) {
        try {
            return getUser(name) != null;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Проверяет, есть ли пользователь с таким id
     *
     * @param id - id пользователя, которое проверяем
     * @return - true - есть, false - нет
     */
    public boolean checkUserId(int id) {
        try {
            return getUser(id) != null;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Проверяет, есть ли пользователь с таким именем и паролем
     *
     * @param name     - имя пользователя, которое проверяем
     * @param password - пароль пользователя, который проверяем
     * @return - true - есть (авторизация прошла успешно), false - нет
     */
    public ExecutionResponse checkUserPass(String name, String password) {
        try {
            if (getUser(name, password) != null){
                return new ExecutionResponse(true, new AnswerString("OK"));
            } else{
                return new ExecutionResponse(false, new AnswerString("Неверный логин или пароль"));
            }

        } catch (SQLException e) {
            return new ExecutionResponse(false, new AnswerString("Пользователь не найден"));
        }
    }
}
