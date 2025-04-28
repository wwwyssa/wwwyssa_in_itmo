package com.wwwyssa.lab6.common.models;
import com.wwwyssa.lab6.common.util.Validatable;
;


/**
 * Класс, представляющий координаты.
 */
public class Coordinates implements Validatable {
    private Integer x; //Максимальное значение поля: 765, Поле не может быть null
    
    private long y; //Значение поля должно быть больше -395

    /**
     * Конструктор для создания объекта Coordinates.
     * @param x координата x, не может быть null и не больше 765
     * @param y координата y, должна быть больше -395
     */
    public Coordinates (Integer x, Long y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates() {}

    /**
     * Проверяет, являются ли координаты допустимыми.
     * @return true, если координаты допустимы, иначе false
     */
    @Override
    public boolean isValid() {
        return x != null && x <= 765 && y > -395;
    }

    /**
     * Возвращает координату x.
     * @return координата x
     */
    public Integer getX() {
        return x;
    }

    /**
     * Возвращает координату y.
     * @return координата y
     */
    public long getY() {
        return y;
    }

    /**
     * Возвращает строковое представление объекта Coordinates.
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Проверяет, равен ли данный объект другому объекту.
     * @param obj объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Coordinates) {
            Coordinates coordinates = (Coordinates) obj;
            return x.equals(coordinates.x) && y == coordinates.y;
        }
        return false;
    }

    /**
     * Возвращает хэш-код объекта Coordinates.
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return x.hashCode() + Long.hashCode(y);
    }
}