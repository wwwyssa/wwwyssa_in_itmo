package models;

import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Objects;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product implements Comparable<Product> {

    @XmlAttribute
    private long id;

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private Coordinates coordinates;

    @XmlElement(required = true)
    private LocalDateTime creationDate = LocalDateTime.now();

    @XmlElement
    private int price;

    @XmlElement
    private String partNumber;

    @XmlElement
    private int manufactureCost;

    @XmlElement(required = true)
    private UnitOfMeasure unitOfMeasure;

    @XmlElement(required = true)
    private Organization manufacturer;

    public Product() {} // Пустой конструктор для JAXB

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

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }

    public int getManufactureCost() { return manufactureCost; }
    public void setManufactureCost(int manufactureCost) { this.manufactureCost = manufactureCost; }

    public UnitOfMeasure getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public Organization getManufacturer() { return manufacturer; }
    public void setManufacturer(Organization manufacturer) { this.manufacturer = manufacturer; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, price, partNumber, manufactureCost, unitOfMeasure, manufacturer);
    }

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

    @Override
    public int compareTo(Product product) {
        return Integer.compare(this.price, product.price);
    }
}
