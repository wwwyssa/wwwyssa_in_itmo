package com.wwwyssa.lab6.server.managers.dbManagers;

import com.wwwyssa.lab6.common.models.*;
import com.wwwyssa.lab6.common.util.User;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ProductDatabaseManager {
    private final DbConnectionManager connectionManager;
    private static volatile ProductDatabaseManager instance;


    public ProductDatabaseManager(String url, String login, String password) {
        connectionManager = new DbConnectionManager(url, login, password);
    }

    public static ProductDatabaseManager getInstance() {
        if (instance == null) {
            synchronized (ProductDatabaseManager.class) {
                if (instance == null) {
                    instance = new ProductDatabaseManager("jdbc:postgresql://localhost:5432/laba7", "postgres", "123");
                }
            }
        }
        return instance;
    }

    public ProductDatabaseManager(DbConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Connection getConnection() throws SQLException {
        return connectionManager.getConnection();
    }

    /**
     * Загружает объектов Product из базы данных
     *
     * @return Map<Integer, Person> - соответствие id и Product
     */
    public Map<Integer, Product> loadProducts() throws SQLException {
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT product.id, product.creator_id, product.name, product.coordinates_id, " +
                        "product.creation_date, product.price, product.part_number, " +
                        "product.manufacture_cost, product.unit_of_measure, product.manufacturer_id, coordinates.x, coordinates.y, " +
                        "manufacturer.name, manufacturer.employees_count, manufacturer.organisation_type, " +
                        "address.street, location.x, location.y, location.z " +
                        "FROM product JOIN coordinates ON product.coordinates_id = coordinates.id " +
                        "JOIN manufacturer ON product.manufacturer_id = manufacturer.id " +
                        "JOIN location ON manufacturer.address_id = location.id " +
                        "JOIN address ON address.location_id = location.id"
        );

        ResultSet result = statement.executeQuery();

        Map<Integer, Product> products = new HashMap<Integer, Product>();
        while (result.next()) {
            Integer id = result.getInt("id");
            Product product = new Product(
                    id,
                    result.getString("name"),
                    new Coordinates(
                            result.getInt("x"),
                            result.getLong("y")
                    ),
                    result.getDate("creation_date") == null ? null : result.getDate("creation_date").toLocalDate().atStartOfDay(),
                    result.getInt("price"),
                    result.getString("part_number"),
                    result.getInt("manufacture_cost"),
                    UnitOfMeasure.valueOf(result.getString("unit_of_measure")),
                    new Organization(
                            result.getLong("manufacturer_id"),
                            result.getString("name"),
                            result.getInt("employees_count"),
                            OrganizationType.valueOf(result.getString("organisation_type")),
                            new Address(
                                    result.getString("street"),
                                    new Location(
                                            result.getFloat("x"),
                                            result.getInt("y"),
                                            result.getInt("z")
                                    )

                            )
                    )
            );

            product.setCreator(result.getInt("creator_id"));
            products.put(id, product);
        }

        connection.close();
        return products;
    }


    public ExecutionResponse addProduct(User user, Product product) throws SQLException {
        Connection connection = getConnection();
        try{
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO product (name, coordinates_id, creation_date, price, part_number, " +
                            "manufacture_cost, unit_of_measure, manufacturer_id, creator_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?::UnitOfMeasure, ?, " +
                            "(SELECT id FROM users WHERE users.name = ?)) " +
                            "RETURNING id"
            );

            statement.setString(1, product.getName());
            statement.setInt(2, addCoordinates(product.getCoordinates())); // Assuming addCoordinates returns the coordinates_id
            statement.setTimestamp(3, Timestamp.valueOf(product.getCreationDate()));
            statement.setInt(4, product.getPrice());
            statement.setString(5, product.getPartNumber());
            statement.setInt(6, product.getManufactureCost());
            statement.setString(7, product.getUnitOfMeasure().toString());
            statement.setInt(8, addManufacturer(product.getManufacturer())); // Assuming addManufacturer returns the manufacturer_id
            statement.setString(9, user.getName()); // Assuming user.getName() returns the creator_id


            ResultSet result = statement.executeQuery();
            connection.close();

            result.next();

            return new ExecutionResponse(true, new AnswerString(result.getString("id")));
        } catch (SQLException e) {
            return new ExecutionResponse(false, new AnswerString(product.getUnitOfMeasure() + " Произошла ошибка при добавлении продукта в базу данных!" + e));
        }

    }

    public int addCoordinates(Coordinates coordinates) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id"
        );

        statement.setInt(1, coordinates.getX());
        statement.setLong(2, coordinates.getY());

        ResultSet result = statement.executeQuery();
        result.next();

        int id = result.getInt("id");
        connection.close();

        return id;


    }

    public int addManufacturer(Organization manufacturer) throws SQLException {
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO manufacturer (name, employees_count, organisation_type, address_id) " +
                        "VALUES (?, ?, ?::OrganisationType, ?) RETURNING id"
        );

        statement.setString(1, manufacturer.getName());
        statement.setInt(2, manufacturer.getEmployeesCount());
        statement.setString(3, manufacturer.getType().toString());
        statement.setInt(4, addAddress(manufacturer.getOfficialAddress())); // Assuming addAddress returns the address_id

        ResultSet result = statement.executeQuery();
        result.next();

        int id = result.getInt("id");
        connection.close();

        return id;
    }


    public int addAddress(Address address) throws SQLException {
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO address (street, location_id) VALUES (?, ?) RETURNING id"
        );

        statement.setString(1, address.getStreet());
        statement.setInt(2, addLocation(address.getTown())); // Assuming addLocation returns the location_id

        ResultSet result = statement.executeQuery();
        result.next();

        int id = result.getInt("id");
        connection.close();

        return id;
    }


    public int addLocation(Location location) throws SQLException {
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO location (x, y, z) VALUES (?, ?, ?) RETURNING id"
        );

        statement.setFloat(1, location.getX());
        statement.setInt(2, location.getY());
        statement.setInt(3, location.getZ());

        ResultSet result = statement.executeQuery();
        result.next();

        int id = result.getInt("id");
        connection.close();

        return id;
    }


    public ExecutionResponse removeProduct(User user, int id) throws SQLException {
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM product WHERE id = ? AND creator_id = (SELECT id FROM users WHERE name = ?)"
        );

        statement.setInt(1, id);
        statement.setString(2, user.getName());

        int rowsAffected = statement.executeUpdate();
        connection.close();

        if (rowsAffected > 0) {
            return new ExecutionResponse(true, new AnswerString("Product removed successfully."));
        } else {
            return new ExecutionResponse(false, new AnswerString("Product not found or you are not the creator."));
        }
    }

    public ExecutionResponse updateProduct(Product product, User user) throws SQLException {
        try {
            int coordinatesId;
            int manufacturerId;

            Connection connection = getConnection();
            String checkQuery = "SELECT COUNT(*) FROM product WHERE id = ?;";

            String updateBand = "UPDATE product SET name = ?, coordinates_id = ?, price = ?, " +
                    "part_number = ?, manufacture_cost = ?, unit_of_measure = ?, manufacturer_id = ?  WHERE id = ? AND user_id IN (SELECT id FROM users WHERE username = ?) RETURNING coordinates_id, manufacturer_id";

            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                 PreparedStatement prodStmt = connection.prepareStatement(updateBand)) {
                checkStmt.setLong(1, product.getId());
                ResultSet checkResult = checkStmt.executeQuery();
                if (checkResult.next() && checkResult.getInt(1) == 0) {
                    return new ExecutionResponse(false, new AnswerString("Элемент с указанным id не найден!"));
                }

                prodStmt.setString(1, product.getName());
                prodStmt.setInt(2, addCoordinates(product.getCoordinates()));
                prodStmt.setInt(3, product.getPrice());
                prodStmt.setString(4, product.getPartNumber());
                prodStmt.setInt(5, product.getManufactureCost());
                prodStmt.setString(6, product.getUnitOfMeasure().toString());
                prodStmt.setInt(7, addManufacturer(product.getManufacturer()));
                ResultSet rs = prodStmt.executeQuery();
                if (rs.next()) {
                    coordinatesId = rs.getInt("coordinates_id");
                    manufacturerId = rs.getInt("manufacturer_id");
                } else {
                    return new ExecutionResponse(false, new AnswerString("Пользователь не является владельцем элемента коллекции!"));
                }
            }
            String updateCoordinates = "UPDATE coordinates SET x = ?, y = ? WHERE id = ?";
            try (PreparedStatement coordinatesStmt = connection.prepareStatement(updateCoordinates)) {
                coordinatesStmt.setDouble(1, product.getCoordinates().getX());
                coordinatesStmt.setLong(2, product.getCoordinates().getY());
                coordinatesStmt.setInt(3, coordinatesId);
                coordinatesStmt.executeUpdate();
            }
            String updateManufacturer = "UPDATE manufacturer SET name = ?, employees_count = ?, organisation_type = ? WHERE id = ?";
            try (PreparedStatement manufacturerStmt = connection.prepareStatement(updateManufacturer)) {
                manufacturerStmt.setString(1, product.getManufacturer().getName());
                manufacturerStmt.setInt(2, product.getManufacturer().getEmployeesCount());
                manufacturerStmt.setString(3, product.getManufacturer().getType().toString());
                manufacturerStmt.setInt(4, manufacturerId);
                manufacturerStmt.executeUpdate();
            }
            return new ExecutionResponse(true, new AnswerString("Элемент коллекции успешно обновлён!"));
        } catch (SQLException e) {
            return new ExecutionResponse(false, new AnswerString("Произошла ошибка при обновлении элемента коллекции!"));
        }
    }
}