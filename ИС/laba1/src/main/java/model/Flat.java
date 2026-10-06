package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "flat")
public class Flat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Positive
    private Integer id;

    @NotBlank
    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coordinates_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_flat_coordinates"))
    private Coordinates coordinates;

    @NotNull
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @NotNull
    @Positive
    @Column(name = "area", nullable = false)
    private Long area;

    @Positive
    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "balcony")
    private Boolean balcony;

    @Positive
    @Column(name = "time_to_metro_on_foot", nullable = false)
    private float timeToMetroOnFoot;

    @Min(1)
    @Max(11)
    @Column(name = "number_of_rooms", nullable = false)
    private int numberOfRooms;

    @Column(name = "furniture")
    private Boolean furniture;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "view_type", nullable = false, length = 16)
    private View view;

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_type", length = 16)
    private Transport transport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", foreignKey = @ForeignKey(name = "fk_flat_house"))
    private House house;

    public Flat() {
    }

    @PrePersist
    private void setCreationDateOnInsert() {
        creationDate = LocalDateTime.now();
    }

    @AssertTrue(message = "Цена должна быть конечным числом больше нуля")
    public boolean isPriceValid() {
        return Double.isFinite(price) && price > 0;
    }

    @AssertTrue(message = "Время до метро должно быть конечным числом больше нуля")
    public boolean isTimeToMetroOnFootValid() {
        return Float.isFinite(timeToMetroOnFoot) && timeToMetroOnFoot > 0;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Long getArea() {
        return area;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Boolean getBalcony() {
        return balcony;
    }

    public void setBalcony(Boolean balcony) {
        this.balcony = balcony;
    }

    public float getTimeToMetroOnFoot() {
        return timeToMetroOnFoot;
    }

    public void setTimeToMetroOnFoot(float timeToMetroOnFoot) {
        this.timeToMetroOnFoot = timeToMetroOnFoot;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Boolean getFurniture() {
        return furniture;
    }

    public void setFurniture(Boolean furniture) {
        this.furniture = furniture;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
