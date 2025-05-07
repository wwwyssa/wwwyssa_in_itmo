package com.wwwyssa.lab6.server.managers;

import com.wwwyssa.lab6.common.models.Product;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Class for managing the collection of products.
 */
public class CollectionManager {
    private static long currentId = 1;
    private final DumpManager dumpManager = DumpManager.getInstance();
    private final CollectionStorage collectionStorage = new CollectionStorage();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private static volatile CollectionManager instance;

    /**
     * Private constructor for singleton pattern.
     */
    private CollectionManager() {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        collectionStorage.getCollection().putAll(dumpManager.readMap());
    }

    /**
     * Returns the singleton instance of CollectionManager.
     * @return The instance of CollectionManager.
     */
    public static CollectionManager getInstance() {
        if (instance == null) {
            instance = new CollectionManager();
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
        collectionStorage.clear();
    }

    /**
     * Returns the collection of products.
     * @return The collection of products.
     */
    public Map<Integer, Product> getCollection() {
        return collectionStorage.getCollection();
    }

    /**
     * Retrieves a product by its ID.
     * @param id The ID of the product.
     * @return The product with the given ID.
     */
    public Product getById(long id) {
        return collectionStorage.readProduct(id);
    }

    /**
     * Checks if a product exists in the collection.
     * @param product The product to check.
     * @return true if the product exists, false otherwise.
     */
    public boolean contains(Product product) {
        return collectionStorage.contains(product);
    }

    /**
     * Returns a free ID for a new product.
     * @return A free ID.
     */
    public long getFreeId() {
        while (collectionStorage.getCollection().containsKey((int) currentId)) {
            currentId++;
        }
        return currentId;
    }

    /**
     * Adds a product to the collection.
     * @param product The product to add.
     * @return true if the product was added successfully, false otherwise.
     */
    public boolean addProduct(Product product) {
        return collectionStorage.createProduct(product);
    }

    /**
     * Adds a product with a specific key to the collection.
     * @param key The key for the product.
     * @param product The product to add.
     * @return true if the product was added successfully.
     */
    public boolean addProductWithKey(int key, Product product) {
        collectionStorage.getCollection().put(key, product);
        return true;
    }

    /**
     * Updates a product in the collection.
     * @param id The ID of the product to update.
     * @param updatedProduct The updated product.
     * @return true if the product was updated successfully, false otherwise.
     */
    public boolean updateProduct(long id, Product updatedProduct) {
        return collectionStorage.updateProduct(id, updatedProduct);
    }

    /**
     * Removes a product from the collection.
     * @param id The ID of the product to remove.
     * @return true if the product was removed successfully, false otherwise.
     */
    public boolean removeProduct(long id) {
        return collectionStorage.deleteProduct(id);
    }

    /**
     * Loads the collection from the dump.
     * @return The status of the load operation.
     */
    public ExecutionResponse loadCollection() {
        collectionStorage.getCollection().putAll(dumpManager.readMap());
        lastInitTime = LocalDateTime.now();
        return new ExecutionResponse(new AnswerString("OK"));
    }

    /**
     * Saves the collection to the dump.
     */
    public void saveCollection() {
        if (!collectionStorage.getCollection().isEmpty()) {
            dumpManager.writeMap(collectionStorage.getCollection());
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
        return collectionStorage.getCollection().getClass().getName();
    }

    /**
     * Returns the size of the collection.
     * @return The size of the collection.
     */
    public int collectionSize() {
        return collectionStorage.getCollection().size();
    }

    @Override
    public String toString() {
        Map<Integer, Product> collection = collectionStorage.getCollection();
        if (collection.isEmpty()) return "Коллекция пуста!";
        StringBuilder info = new StringBuilder();
        for (int id : collection.keySet()) {
            info.append(collection.get(id)+"\n\n");
        }
        return info.toString().trim();
    }

}
