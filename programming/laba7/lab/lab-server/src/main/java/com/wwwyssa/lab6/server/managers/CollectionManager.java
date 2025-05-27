package com.wwwyssa.lab6.server.managers;

import com.wwwyssa.lab6.common.models.Product;
import com.wwwyssa.lab6.common.util.User;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.server.managers.dbManagers.ProductDatabaseManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class for managing the collection of products.
 */
public class CollectionManager {
    private static long currentId = 1;
    private final Map<Integer, Product> collection = new LinkedHashMap<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private static volatile CollectionManager instance;
    private final ProductDatabaseManager productDatabaseManager = ProductDatabaseManager.getInstance();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Private constructor for singleton pattern.
     */
    private CollectionManager() {}

    /**
     * Returns the singleton instance of CollectionManager.
     * @return The instance of CollectionManager.
     */
    public static CollectionManager getInstance() {
        if (instance == null) {
            synchronized (CollectionManager.class) {
                if (instance == null) {
                    instance = new CollectionManager();
                }
            }
        }
        return instance;
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
    public Map<Integer, Product> getCollection() {
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
        return collection.get((int) id);
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

        try {
            if ((product != null) && product.isValid()) {
                ExecutionResponse addStatus = productDatabaseManager.addProduct(user, product);
                if (addStatus.getExitCode()) {
                    product.setId(Long.parseLong((String) addStatus.getAnswer().getAnswer()));
                    lastSaveTime = LocalDateTime.now();
                    collection.put((int) product.getId(), product);
                    return new ExecutionResponse<>(true, new AnswerString("Элемент успешно добавлен в коллекцию! Присвоенный id = " + addStatus.getAnswer()));
                }
                return new ExecutionResponse<>(false, new AnswerString( "Произошла ошибка при добавлении коллекции в базу данных!"));
            }
            return new ExecutionResponse(false,  new AnswerString("Элемент коллекции введён неверно!"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {

        }




    }

    /**
     * Updates a product in the collection.
     * @param id The ID of the product to update.
     * @param updatedProduct The updated product.
     * @return true if the product was updated successfully, false otherwise.
     */
    public boolean updateProduct(long id, Product updatedProduct, User user) {
        lock.writeLock().lock();
        if (!collection.containsKey((int) id)) {
            return false; // Product with the given ID does not exist
        }

        collection.put((int) id, updatedProduct);
        try {
            productDatabaseManager.updateProduct(updatedProduct, user);
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при обновлении элемента в базе данных!");
            return false;
        } finally {
            lock.writeLock().unlock();
        }
        lock.writeLock().unlock();
        return true;
    }

    /**
     * Removes a product from the collection.
     * @param id The ID of the product to remove.
     * @return true if the product was removed successfully, false otherwise.
     */
    public ExecutionResponse removeProduct(long id, User user) {
        lock.writeLock().lock();
        if (!collection.containsKey((int) id)) {
            return new ExecutionResponse(false, new AnswerString("Product with the given ID does not exist")); // Product with the given ID does not exist
        }

        try {
            ExecutionResponse resp = productDatabaseManager.removeProduct(user, (int) id);
            if (resp.getExitCode()){
                collection.remove((int) id);
                return new ExecutionResponse(true, new AnswerString("Product removed successfully"));
            }else{
                return new ExecutionResponse(false, new AnswerString("Product not found or you are not the creator"));
            }
        } catch (SQLException e) {
            return new ExecutionResponse (false, new AnswerString("Произошла ошибка при удалении элемента из базы данных!"));
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Loads the collection from the dump.
     * @return The status of the load operation.
     */
    public ExecutionResponse loadCollection() throws SQLException {
        collection.putAll(productDatabaseManager.loadProducts());
        lastInitTime = LocalDateTime.now();
        return new ExecutionResponse(new AnswerString("OK"));
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
        Map<Integer, Product> collection = this.collection;
        if (collection.isEmpty()) return "Коллекция пуста!";
        StringBuilder info = new StringBuilder();
        for (int id : collection.keySet()) {
            info.append(collection.get(id)+"\n\n");
        }
        return info.toString().trim();
    }

}
