package com.wwwyssa.lab6.server.managers.dbManagers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс, отвечающий за подключение к базе данных.
 */
public class DbConnectionManager {
    private final String url;
    private final String login;
    private final String password;

    public DbConnectionManager(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, login, password);
    }
}
