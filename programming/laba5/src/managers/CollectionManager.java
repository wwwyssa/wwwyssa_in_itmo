package managers;

import models.Product;

import java.util.LinkedHashMap;

public class CollectionManager {
    private static long currentId = 1;
    private LinkedHashMap<Integer, Product> collection = new LinkedHashMap<>();
    public static long generateId() {
        return currentId++;
    }

}
