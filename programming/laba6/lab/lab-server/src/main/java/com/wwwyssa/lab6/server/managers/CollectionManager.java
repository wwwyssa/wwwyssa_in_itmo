package com.wwwyssa.lab6.server.managers;

import com.wwwyssa.lab6.common.models.Product;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Класс для управления коллекцией продуктов.
 */
public class CollectionManager {
    private static long currentId = 1;
    private Map<Integer, Product> collection = new LinkedHashMap<>();
    private final DumpManager dumpManager = DumpManager.getInstance();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private static volatile CollectionManager instance;

    /**
     * Конструктор для создания объекта CollectionManager.
     */
    public CollectionManager() {
        this.lastInitTime = null;
        this.lastSaveTime = null;
    }


    /**
     * Возвращает единственный экземпляр CollectionManager.
     * @return Экземпляр CollectionManager.
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
     * Генерирует новый уникальный идентификатор.
     * @return новый уникальный идентификатор
     */
    public static long generateId() {
        return currentId++;
    }

    /**
     * Очищает коллекцию.
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Возвращает коллекцию продуктов.
     * @return коллекция продуктов
     */
    public Map<Integer, Product> getCollection() {
        return collection;
    }

    /**
     * Возвращает продукт по его идентификатору.
     * @param id идентификатор продукта
     * @return продукт с заданным идентификатором
     */
    public Product getById(long id) {
        return collection.get((int) id);
    }

    /**
     * Проверяет, содержится ли продукт в коллекции.
     * @param product продукт для проверки
     * @return true, если продукт содержится в коллекции, иначе false
     */
    public boolean contains(Product product) {
        return collection.containsValue(product);
    }

    /**
     * Возвращает свободный идентификатор.
     * @return свободный идентификатор
     */
    public long getFreeId() {
        while (collection.containsKey((int) currentId)) {
            currentId++;
        }
        return currentId;
    }

    /**
     * Добавляет продукт в коллекцию.
     * @param product продукт для добавления
     * @return true, если продукт успешно добавлен, иначе false
     */
    public boolean addProduct(Product product) {
        if (contains(product)) {
            return false;
        }
        collection.put((int) product.getId(), product);
        return true;
    }

    public boolean addProductWithKey(int key, Product product) {
        collection.put(key, product);
        return true;
    }


    /**
     * Обновляет продукт в коллекции.
     * @param product продукт для обновления
     */
    public void updateProduct(Product product) {
        collection.put((int) product.getId(), product);
    }

    /**
     * Удаляет продукт из коллекции.
     * @param id id продукта для удаления
     */
    public void removeProduct(long id) {
        collection.remove((int) id);
    }

    /**
     * Загружает коллекцию из дампа данных.
     */
    public ExecutionResponse loadCollection() {
        collection = dumpManager.readMap();
        lastInitTime = LocalDateTime.now();
        return new ExecutionResponse(new AnswerString("OK"));
    }

    /**
     * Сохраняет коллекцию в дамп данных.
     */
    public void saveCollection() {
        if (!collection.isEmpty()){
            dumpManager.writeMap(collection);
            lastSaveTime = LocalDateTime.now();
        }
    }

    /**
     * Возвращает время последней инициализации коллекции.
     * @return время последней инициализации
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * Возвращает время последнего сохранения коллекции.
     * @return время последнего сохранения
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * Возвращает количество элементов в коллекции.
     * @return количество элементов
     */
    public String collectionType() {
        return collection.getClass().getName();
    }


    /**
     * Возвращает количество элементов в коллекции.
     * @return количество элементов
     */
    public int collectionSize() {
        return collection.size();
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        StringBuilder info = new StringBuilder();
        for (int id : collection.keySet()) {
            info.append(collection.get(id)+"\n\n");
        }
        return info.toString().trim();
    }



}