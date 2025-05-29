package com.wwwyssa.lab7.server.managers;

import com.wwwyssa.lab7.common.models.*;
import com.wwwyssa.lab7.common.util.User;
import com.wwwyssa.lab7.common.util.executions.AnswerString;
import com.wwwyssa.lab7.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab7.server.Server;
import com.wwwyssa.lab7.server.dao.ProductDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Class for managing the collection of products.
 */
public class CollectionManager {
    private static long currentId = 1;
    private final Map<Long, Product> collection = new LinkedHashMap<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private static volatile CollectionManager instance;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final PersistenceManager persistenceManager;

    /**
     * Constructor for CollectionManager.
     * @param persistenceManager The persistence manager to handle database operations.
     */
    public CollectionManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;

        try {
            load();
        } catch (Exception e) {
            Server.logger.info("Ошибка загрузки продуктов из базы данных!" + e);
            System.exit(1);
        }
    }



    private void load() {
        Server.logger.info("Загрузка начата...");
        lock.readLock().lock();
        List<ProductDAO> daos = persistenceManager.loadProducts();

        var products = daos.stream().map((dao) -> {
            Organization manufacturer = null;
            if (dao.getManufacturer() != null) {
                manufacturer = new Organization(
                        dao.getManufacturer().getId(),
                        dao.getManufacturer().getName(),
                        dao.getManufacturer().getEmployeesCount(),
                        dao.getManufacturer().getType(),
                        new Address(dao.getManufacturer().getStreet(), new Location(
                                dao.getManufacturer().getX(),
                                dao.getManufacturer().getY(),
                                dao.getManufacturer().getZ()
                        ))
                );
            }
            return new Product(
                    dao.getId(),
                    dao.getName(),
                    new Coordinates(dao.getX(), dao.getY()),
                    dao.getCreationDate(),
                    dao.getPrice(),
                    dao.getPartNumber(),
                    dao.getManufactureCost(),
                    dao.getUnitOfMeasure(),
                    manufacturer,
                    dao.getCreator().getId()
            );
        }).collect(Collectors.toMap(Product::getId, product -> product));

        collection.putAll(products);
        lastInitTime = LocalDateTime.now();
        lock.readLock().unlock();

        Server.logger.info("Загрузка завершена!");
    }



    /**
     * Generates a new unique ID.
     * @return A new unique ID.
     */
    public static long generateId() {
        return currentId++;
    }

    /**
     * Clears the collection.
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Returns the collection of products.
     * @return The collection of products.
     */
    public Map<Long, Product> getCollection() {
        lock.readLock().lock(); // Блокировка чтения
        try {
            return collection;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Retrieves a product by its ID.
     * @param id The ID of the product.
     * @return The product with the given ID.
     */
    public Product getById(long id) {
        lock.readLock().lock(); // Блокировка чтения
        try {
        return collection.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Checks if a product exists in the collection.
     * @param product The product to check.
     * @return true if the product exists, false otherwise.
     */
    public boolean contains(Product product) {
        return collection.containsValue(product);
    }

    /**
     * Returns a free ID for a new product.
     * @return A free ID.
     */
    public long getFreeId() {
        while (collection.containsKey((int) currentId)) {
            currentId++;
        }
        return currentId;
    }

    /**
     * Adds a product to the collection.
     * @param product The product to add.
     * @return true if the product was added successfully, false otherwise.
     */
    public ExecutionResponse addProduct(Product product, User user) {
        Server.logger.info("Добавление продукта в коллекцию..." + user.getName());
        try {

            if ((product != null) && product.isValid()) {
                ExecutionResponse addStatus = persistenceManager.add(user, product);
                if (addStatus.getExitCode()) {
                    product.setId(Long.parseLong((String) addStatus.getAnswer().getAnswer()));
                    lastSaveTime = LocalDateTime.now();
                    collection.put(product.getId(), product);
                    return new ExecutionResponse<>(true, new AnswerString("Элемент успешно добавлен в коллекцию! Присвоенный id = " + addStatus.getAnswer()));
                }
                return new ExecutionResponse<>(false, new AnswerString( "Произошла ошибка при добавлении коллекции в базу данных!"));
            }
            return new ExecutionResponse(false,  new AnswerString("Элемент коллекции введён неверно!"));
        }  finally {

        }




    }


    /**
     * Removes a product from the collection.
     * @param id The ID of the product to remove.
     * @return true if the product was removed successfully, false otherwise.
     */
    public ExecutionResponse removeProduct(long id, User user) {
        lock.writeLock().lock();
        if (!collection.containsKey(id)) {
            return new ExecutionResponse(false, new AnswerString("Product with the given ID does not exist")); // Product with the given ID does not exist
        }


        if (getById(id).getCreator() != user.getId()) {
            Server.logger.info("Другой владелец.");
            return new ExecutionResponse(false, new AnswerString("Вы не можете удалить продукт, который не принадлежит вам!"));
        }
        int removedCount = persistenceManager.remove(user, id);
        if (removedCount == 0) {
            Server.logger.info("Ничего не было удалено.");
            return new ExecutionResponse(false, new AnswerString("Ничего не было удалено!"));
        }
        lock.writeLock().lock();
        collection.remove(id);
        lock.writeLock().unlock();
        lastSaveTime = LocalDateTime.now();
        return new ExecutionResponse<>(true, new AnswerString("Продукт успешно удалён!"));


    }



    /**
     * Saves the collection to the dump.
     */
    public void saveCollection() {
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста, сохранение невозможно!");
            lastSaveTime = LocalDateTime.now();
        }
    }

    /**
     * Returns the time of the last initialization.
     * @return The time of the last initialization.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * Returns the time of the last save.
     * @return The time of the last save.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * Returns the type of the collection.
     * @return The type of the collection.
     */
    public String collectionType() {
        return collection.getClass().getName();
    }

    /**
     * Returns the size of the collection.
     * @return The size of the collection.
     */
    public int collectionSize() {
        return collection.size();
    }

    @Override
    public String toString() {
        Map<Long, Product> collection = this.collection;
        if (collection.isEmpty()) return "Коллекция пуста!";
        StringBuilder info = new StringBuilder();
        for (long id : collection.keySet()) {
            info.append(collection.get(id)+"\n\n");
        }
        return info.toString().trim();
    }

}
