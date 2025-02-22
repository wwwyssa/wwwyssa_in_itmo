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

    public float getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Location) {
            Location location = (Location) obj;
            return x == location.x && y == location.y && z.equals(location.z);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(x) + Integer.hashCode(y) + z.hashCode();
    }
}
