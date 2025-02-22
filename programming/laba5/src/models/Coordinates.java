package models;
import utils.Validatable;

public class Coordinates implements Validatable {
    private Integer x; //Максимальное значение поля: 765, Поле не может быть null
    private long y; //Значение поля должно быть больше -395

    public Coordinates (Integer x, Long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isValid() {
        return x != null && x <= 765 && y > -395;
    }

    public Integer getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Coordinates) {
            Coordinates coordinates = (Coordinates) obj;
            return x.equals(coordinates.x) && y == coordinates.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return x.hashCode() + Long.hashCode(y);
    }
}
