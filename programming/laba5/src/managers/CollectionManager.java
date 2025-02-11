package managers;

public class CollectionManager {
    private static long currentId = 1;
    public static long generateId() {
        return currentId++;
    }
}
