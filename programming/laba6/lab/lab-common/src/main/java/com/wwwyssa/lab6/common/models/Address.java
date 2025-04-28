package com.wwwyssa.lab6.common.models;

import com.wwwyssa.lab6.common.util.Validatable;


/**
 * Класс, представляющий адрес.
 */
public class Address implements Validatable {

    private String street; // Поле не может быть null

    private Location town; // Поле может быть null

    /**
     * Конструктор класса Address.
     * @param street Улица (не может быть null).
     * @param town Город (может быть null).
     */
    public Address(String street, Location town) {
        this.street = street;
        this.town = town;
    }
    /**
     * Проверяет, является ли адрес валидным.
     * @return true, если адрес валиден, иначе false.
     */
    @Override
    public boolean isValid() {
        return street != null && !street.isEmpty() && town != null; // Улучшил условие валидации (проверка на пустоту)
    }

    /**
     * Возвращает улицу.
     * @return Улица.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Возвращает город.
     * @return Город.
     */
    public Location getTown() {
        return town;
    }

    /**
     * Возвращает строковое представление адреса.
     * @return Строковое представление адреса.
     */
    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", town=" + town + '\n' +
                '}';
    }

    /**
     * Проверяет равенство текущего объекта с другим объектом.
     * @param obj Объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Address) {
            Address address = (Address) obj;
            return street.equals(address.street) && town.equals(address.town);
        }
        return false;
    }

    /**
     * Возвращает хэш-код для адреса.
     * @return Хэш-код.
     */
    @Override
    public int hashCode() {
        return street.hashCode() + (town != null ? town.hashCode() : 0);
    }
}
