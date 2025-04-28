package com.wwwyssa.lab6.common.models;

import com.wwwyssa.lab6.common.util.Validatable;

/**
 * Класс, представляющий местоположение.
 */
public class Location implements Validatable {

    
    private float x; // Поле не может быть null

    
    private int y; // Поле не может быть null

    
    private Integer z; // Поле может быть null, но не должно быть

    /**
     * Конструктор для создания объекта Location.
     * @param x координата x
     * @param y координата y
     * @param z координата z, не может быть null
     */
    public Location(float x, int y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location() {};
    /**
     * Возвращает координату x.
     * @return координата x
     */
    public float getX() {
        return x;
    }



    /**
     * Возвращает координату y.
     * @return координата y
     */
    public int getY() {
        return y;
    }

    /**
     * Возвращает координату z.
     * @return координата z
     */
    public Integer getZ() {
        return z;
    }

    /**
     * Возвращает строковое представление объекта Location.
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
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
        if (obj instanceof Location) {
            Location location = (Location) obj;
            return x == location.x && y == location.y && (z != null ? z.equals(location.z) : location.z == null);
        }
        return false;
    }

    /**
     * Возвращает хэш-код объекта Location.
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Float.hashCode(x) + Integer.hashCode(y) + z.hashCode();
    }

    /**
     * Проверяет, является ли объект валидным.
     * @return true, если объект валиден, иначе false
     */
    @Override
    public boolean isValid() {
        return z != null;
    }
}
