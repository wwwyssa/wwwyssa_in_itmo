package com.wwwyssa.lab7.server.managers.dbManagers;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс для работы с ddl-запросами по типу создания таблиц и удаления таблиц.
 */
public class DDLManager {
    private final DbConnectionManager connectionManager;

    public DDLManager(String url, String login, String password) {
        connectionManager = new DbConnectionManager(url, login, password);
    }

    public Connection getConnection() throws SQLException {
        return connectionManager.getConnection();
    }

    public void dropTables() throws SQLException {
        String query = """
                BEGIN;

                DROP TABLE IF EXISTS coordinates CASCADE;
                DROP TABLE IF EXISTS Product CASCADE;
                
                DROP TABLE IF EXISTS manufacturer CASCADE;
                DROP TABLE IF EXISTS location CASCADE;
                DROP TABLE IF EXISTS address CASCADE;

                DROP TABLE IF EXISTS users CASCADE;
                DROP TABLE IF EXISTS commands CASCADE;

                DROP TYPE IF EXISTS OrganisationType CASCADE;
                DROP TYPE IF EXISTS UnitOfMeasure CASCADE;

                END;
                """;
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        connection.close();
        System.out.println("Таблицы удалены");
    }



    public void createTables() throws SQLException {
        String query = """
            BEGIN;
                            
            CREATE TYPE OrganisationType AS ENUM (
                'COMMERCIAL', 'PUBLIC', 'GOVERNMENT', 'PRIVATE_LIMITED_COMPANY', 'OPEN_JOINT_STOCK_COMPANY'
                );
                            
            CREATE TYPE UnitOfMeasure AS ENUM (
                'KILOGRAMS', 'CENTIMETERS', 'MILLILITERS', 'GRAMS'
                );
                            
            CREATE TABLE IF NOT EXISTS users
            (
                id              SERIAL PRIMARY KEY,
                name            VARCHAR(40) UNIQUE      NOT NULL,
                password_digest VARCHAR(96)             NOT NULL,
                salt            VARCHAR(10)             NOT NULL
            );
                            
                            
            CREATE TABLE IF NOT EXISTS Product
            (
                id         SERIAL PRIMARY KEY,
                name       TEXT,
                x INTEGER,
                y INTEGER,
                creation_date TIMESTAMP DEFAULT NOW() NOT NULL,
                price      INTEGER NOT NULL CONSTRAINT positive_price CHECK (price > 0),
                part_number TEXT NOT NULL,
                manufacture_cost INTEGER,
                unit_of_measure TEXT,
                manufacturer_id INTEGER,
                creator_id INTEGER NOT NULL
            );
                            
             
            CREATE TABLE IF NOT EXISTS manufacturer
            (
                id SERIAL PRIMARY KEY,
                name TEXT NOT NULL,
                employees_count INTEGER NOT NULL CONSTRAINT positive_employees_count CHECK (employees_count > 0),
                organisation_type TEXT NOT NULL,
                x FLOAT NOT NULL,
                y INTEGER NOT NULL,
                z INTEGER NOT NULL,
                street TEXT NOT NULL,
                CONSTRAINT positive_z CHECK (z > 0),
                creator_id INTEGER NOT NULL
            );
            
            
            
            ALTER TABLE Product ADD CONSTRAINT fk_manufacturer 
                FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id);
                            
            COMMIT;""";
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        connection.close();
        System.out.println("Таблицы созданы");
    }
}
