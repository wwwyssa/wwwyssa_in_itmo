package com.wwwyssa.lab6.server.managers;

import com.wwwyssa.lab6.common.models.Product;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class for managing the storage of the collection.
 */
public class CollectionStorage {
    private final Map<Integer, Product> collection = new LinkedHashMap<>();

    /**
     * Adds a new product to the collection.
     * @param product The product to add.
     * @return true if the product was added successfully, false if it already exists.
     */
    public boolean createProduct(Product product) {
        if (collection.containsValue(product)) {
            return false; // Product already exists
        }
        collection.put((int) product.getId(), product);
        return true;
    }

    /**
     * Retrieves a product by its ID.
     * @param id The ID of the product.
     * @return The product if found, or null if not found.
     */
    public Product readProduct(long id) {
        return collection.get((int) id);
    }

    /**
     * Updates an existing product in the collection.
     * @param id The ID of the product to update.
     * @param updatedProduct The updated product.
     * @return true if the product was updated successfully, false if it does not exist.
     */
    public boolean updateProduct(long id, Product updatedProduct) {
        if (!collection.containsKey((int) id)) {
            return false; // Product with the given ID does not exist
        }
        collection.put((int) id, updatedProduct);
        return true;
    }

    /**
     * Deletes a product from the collection by its ID.
     * @param id The ID of the product to delete.
     * @return true if the product was deleted successfully, false if it does not exist.
     */
    public boolean deleteProduct(long id) {
        if (!collection.containsKey((int) id)) {
            return false; // Product with the given ID does not exist
        }
        collection.remove((int) id);
        return true;
    }

    /**
     * Clears the entire collection.
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Returns the entire collection.
     * @return The collection of products.
     */
    public Map<Integer, Product> getCollection() {
        return collection;
    }

    /**
     * Checks if a product exists in the collection.
     * @param product The product to check.
     * @return true if the product exists, false otherwise.
     */
    public boolean contains(Product product) {
        return collection.containsValue(product);
    }
}