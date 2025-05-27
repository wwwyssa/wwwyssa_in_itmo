package com.wwwyssa.lab6.server;

import com.wwwyssa.lab6.common.models.*;
import com.wwwyssa.lab6.common.util.User;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.server.managers.dbManagers.DDLManager;
import com.wwwyssa.lab6.server.managers.dbManagers.ProductDatabaseManager;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class test {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/laba7";
        String user = "postgres";
        String password = "123";
        DDLManager ddlManager = new DDLManager(url, user, password);
        User userDb = new User("1234", "1234");
        ProductDatabaseManager productDatabaseManager = new ProductDatabaseManager(url, user, password);
        ExecutionResponse resp1 = productDatabaseManager.addProduct(userDb, new Product(1, "2", new Coordinates(1, 1L), LocalDateTime.now(), 1, "1", 1, UnitOfMeasure.KILOGRAMS, new Organization(1, "1", 1, OrganizationType.COMMERCIAL, new Address("1", new Location(1, 1, 1)))));
        ExecutionResponse resp2 = productDatabaseManager.removeProduct(userDb, 8);
        System.out.println(resp1.getAnswer().getAnswer());
        System.out.println(resp2.getAnswer().getAnswer());
    }
}
