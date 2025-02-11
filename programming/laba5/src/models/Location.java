package models;

public class Location {
    private float x;
    private int y;
    private Integer z; //Поле не может быть null

    public Location(float x, int y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
