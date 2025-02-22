package managers;

import models.Product;
import java.util.LinkedHashMap;

public class CollectionManager {
    private static long currentId = 1;
    private LinkedHashMap<Integer, Product> collection = new LinkedHashMap<>();
    private DumpManager dumpManager;

    public CollectionManager(DumpManager dumpManager) {
        this.dumpManager = dumpManager;
    }

    public static long generateId() {
        return currentId++;
    }


    public void clear() {
        collection.clear();
    }

    public LinkedHashMap<Integer, Product> getCollection() {
        return collection;
    }

    public Product getById(long id) {
        return collection.get((int) id);
    }

    public boolean contains(Product product) {
        return collection.containsValue(product);
    }

    public long getFreeId(){
        while(collection.containsKey((int) currentId)){
            currentId++;
        }
        return currentId;
    }


    public boolean addProduct(Product product) {
        if (contains(product)) {
            return false;
        }
        collection.put((int) product.getId(), product);
        return true;
    }

    public void updateProduct(Product product) {
        collection.put((int) product.getId(), product);
    }

    public void removeProduct(Product product) {
        collection.remove((int) product.getId());
    }

    public void loadCollection() {
        collection = dumpManager.readMap();
    }

    public void saveCollection() {
        dumpManager.writeMap(collection);
    }

}
