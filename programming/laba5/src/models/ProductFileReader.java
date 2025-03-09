package models;

import managers.CollectionManager;

public class ProductFileReader {
    String filename;
    CollectionManager collectionManager;

    public ProductFileReader(String filename, CollectionManager collectionManager) {
        this.filename = filename;
        this.collectionManager = collectionManager;
    }
}
