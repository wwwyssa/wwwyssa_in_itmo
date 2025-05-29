package com.wwwyssa.lab7.server.dao;


import com.wwwyssa.lab7.common.models.Product;
import com.wwwyssa.lab7.common.models.UnitOfMeasure;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name="product")
@Table(name="product", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
public class ProductDAO implements Serializable {
    public ProductDAO() {
    }

    public ProductDAO(Product product) {
        this.name = product.getName();
        this.x = product.getCoordinates().getX();
        this.y = product.getCoordinates().getY();
        this.creationDate = LocalDateTime.from(product.getCreationDate());
        this.price = product.getPrice();
        this.partNumber = product.getPartNumber();
        this.manufactureCost = product.getManufactureCost();
        this.unitOfMeasure = product.getUnitOfMeasure();
    }

    public void update(Product product) {
        this.name = product.getName();
        this.x = product.getCoordinates().getX();
        this.y = product.getCoordinates().getY();
        this.creationDate = LocalDateTime.from(product.getCreationDate());
        this.price = product.getPrice();
        this.partNumber = product.getPartNumber();
        this.unitOfMeasure = product.getUnitOfMeasure();
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable=false, unique=true, length=11)
    private int id;

    @NotBlank(message = "Название продукта не должно быть пустым.")
    @Column(name="name", nullable=false)
    private String name; // Поле не может быть null, Строка не может быть пустой

    @Column(name="x", nullable=false)
    private int x;

    @Column(name="y", nullable=false)
    private int y;

    @Column(name="creation_date", nullable=false)
    private LocalDateTime creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Min(value = 1, message = "Цена должна быть больше нуля.")
    @Column(name="price", nullable=false)
    private int price; // Поле не может быть null, Значение поля должно быть больше 0

    @NotBlank(message = "Part number не должно быть пустым.")
    @Column(name="part_number")
    private String partNumber; // Строка не может быть пустой, Поле может быть null

    @Column(name="unit_of_measure")
    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unitOfMeasure; // Поле может быть null

    @Column(name="manufacture_cost")
    @Min(value = 1, message = "Manufacture cost должен быть больше нуля.")
    private int manufactureCost; // Поле не может быть null, Значение поля должно быть больше 0


    @ManyToOne
    @JoinColumn(name="manufacturer_id")
    private OrganizationDAO manufacturer; // Поле может быть null

    @ManyToOne
    @JoinColumn(name="creator_id")
    private UserDAO creator;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public int getManufactureCost() {
        return this.manufactureCost;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public OrganizationDAO getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(OrganizationDAO manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCreator(UserDAO creator) {
        this.creator = creator;
    }

    public UserDAO getCreator() {
        return creator;
    }

}
