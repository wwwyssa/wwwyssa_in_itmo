package com.wwwyssa.lab6.common.models;

import com.wwwyssa.lab6.common.util.Validatable;


import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс, представляющий продукт.
 */
public class Product implements Comparable<Product>, Validatable {


    private long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически


    private String name; // Поле не может быть null, Строка не может быть пустой


    private Coordinates coordinates; // Поле не может быть null


    private LocalDateTime creationDate = LocalDateTime.now(); // Поле не может быть null


    private int price; // Значение поля должно быть больше 0


    private String partNumber; // Поле не может быть null


    private int manufactureCost;


    private UnitOfMeasure unitOfMeasure; // Поле не может быть null


    private Organization manufacturer; // Поле может быть null

    /**
     * Пустой конструктор для JAXB.
     */
    public Product() {}

    /**
     * Конструктор для создания объекта Product.
     * @param id идентификатор продукта, должен быть больше 0
     * @param name имя продукта, не может быть null или пустым
     * @param coordinates координаты продукта, не могут быть null
     * @param creationDate дата создания продукта, не может быть null
     * @param price цена продукта, должна быть больше 0
     * @param partNumber номер детали, не может быть null
     * @param manufactureCost стоимость производства
     * @param unitOfMeasure единица измерения, не может быть null
     * @param manufacturer производитель, может быть null
     */
    public Product(long id, String name, Coordinates coordinates, LocalDateTime creationDate,
                   int price, String partNumber, int manufactureCost,
                   UnitOfMeasure unitOfMeasure, Organization manufacturer) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.partNumber = partNumber;
        this.manufactureCost = manufactureCost;
        this.unitOfMeasure = unitOfMeasure;
        this.manufacturer = manufacturer;
    }

    // Геттеры и сеттеры

    /**
     * Возвращает идентификатор продукта.
     * @return идентификатор продукта
     */
    public long getId() { return id; }

    /**
     * Устанавливает идентификатор продукта.
     * @param id идентификатор продукта
     */
    public void setId(long id) { this.id = id; }

    /**
     * Возвращает имя продукта.
     * @return имя продукта
     */
    public String getName() { return name; }

    /**
     * Устанавливает имя продукта.
     * @param name имя продукта
     */
    public void setName(String name) { this.name = name; }

    /**
     * Возвращает координаты продукта.
     * @return координаты продукта
     */
    public Coordinates getCoordinates() { return coordinates; }

    /**
     * Устанавливает координаты продукта.
     * @param coordinates координаты продукта
     */
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }

    /**
     * Возвращает дату создания продукта.
     * @return дата создания продукта
     */
    public LocalDateTime getCreationDate() { return creationDate; }

    /**
     * Устанавливает дату создания продукта.
     * @param creationDate дата создания продукта
     */
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    /**
     * Возвращает цену продукта.
     * @return цена продукта
     */
    public int getPrice() { return price; }

    /**
     * Устанавливает цену продукта.
     * @param price цена продукта
     */
    public void setPrice(int price) { this.price = price; }

    /**
     * Возвращает номер детали продукта.
     * @return номер детали продукта
     */
    public String getPartNumber() { return partNumber; }

    /**
     * Устанавливает номер детали продукта.
     * @param partNumber номер детали продукта
     */
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }

    /**
     * Возвращает стоимость производства продукта.
     * @return стоимость производства продукта
     */
    public int getManufactureCost() { return manufactureCost; }

    /**
     * Устанавливает стоимость производства продукта.
     * @param manufactureCost стоимость производства продукта
     */
    public void setManufactureCost(int manufactureCost) { this.manufactureCost = manufactureCost; }

    /**
     * Возвращает единицу измерения продукта.
     * @return единица измерения продукта
     */
    public UnitOfMeasure getUnitOfMeasure() { return unitOfMeasure; }

    /**
     * Устанавливает единицу измерения продукта.
     * @param unitOfMeasure единица измерения продукта
     */
    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    /**
     * Возвращает производителя продукта.
     * @return производитель продукта
     */
    public Organization getManufacturer() { return manufacturer; }

    /**
     * Устанавливает производителя продукта.
     * @param manufacturer производитель продукта
     */
    public void setManufacturer(Organization manufacturer) { this.manufacturer = manufacturer; }

    /**
     * Проверяет, равен ли данный объект другому объекту.
     * @param o объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    /**
     * Возвращает хэш-код объекта Product.
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, price, partNumber, manufactureCost, unitOfMeasure, manufacturer);
    }

    /**
     * Возвращает строковое представление объекта Product.
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", partNumber='" + partNumber + '\'' +
                ", manufactureCost=" + manufactureCost +
                ", unitOfMeasure=" + unitOfMeasure +
                ", manufacturer=" + manufacturer +
                '}';
    }

    /**
     * Сравнивает данный объект с другим объектом Product по цене.
     * @param product объект для сравнения
     * @return результат сравнения
     */
    @Override
    public int compareTo(Product product) {
        return Integer.compare(this.price, product.price);
    }

    /**
     * Проверяет, является ли продукт допустимым.
     * @return true, если продукт допустим, иначе false
     */
    @Override
    public boolean isValid() {
        if (id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null || !coordinates.isValid()) return false;
        if (creationDate == null) return false;
        if (price <= 0) return false;
        if (partNumber == null) return false;
        if (unitOfMeasure == null) return false;
        if (manufacturer == null || !manufacturer.isValid()) return false;
        return true;
    }
}