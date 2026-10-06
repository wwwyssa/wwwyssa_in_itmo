package dto;

import jakarta.json.bind.annotation.JsonbNillable;
import java.time.LocalDateTime;
import java.util.Objects;

import model.Flat;
import model.Transport;
import model.View;

@JsonbNillable
public class FlatData {
    public Integer id;
    public String name;
    public Long coordinatesId;
    public Float x;
    public Float y;
    public LocalDateTime creationDate;
    public Long area;
    public double price;
    public Boolean balcony;
    public float timeToMetroOnFoot;
    public int numberOfRooms;
    public Boolean furniture;
    public View view;
    public Transport transport;
    public Long houseId;
    public String houseName;
    public Long houseYear;
    public Integer houseNumberOfFloors;

    public FlatData(Flat flat) {
        this.id = flat.getId();
        this.name = flat.getName();
        this.coordinatesId = flat.getCoordinates().getId();
        this.x = flat.getCoordinates().getX();
        this.y = flat.getCoordinates().getY();
        this.creationDate = flat.getCreationDate();
        this.area = flat.getArea();
        this.price = flat.getPrice();
        this.balcony = flat.getBalcony();
        this.timeToMetroOnFoot = flat.getTimeToMetroOnFoot();
        this.numberOfRooms = flat.getNumberOfRooms();
        this.furniture = flat.getFurniture();
        this.view = flat.getView();
        this.transport = flat.getTransport();
        if (flat.getHouse() != null) {
            this.houseId = flat.getHouse().getId();
            this.houseName = flat.getHouse().getName();
            this.houseYear = flat.getHouse().getYear();
            this.houseNumberOfFloors = flat.getHouse().getNumberOfFloors();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlatData flatData = (FlatData) o;
        return Double.compare(price, flatData.price) == 0 && Float.compare(timeToMetroOnFoot, flatData.timeToMetroOnFoot) == 0 && numberOfRooms == flatData.numberOfRooms && Objects.equals(id, flatData.id) && Objects.equals(name, flatData.name) && Objects.equals(coordinatesId, flatData.coordinatesId) && Objects.equals(x, flatData.x) && Objects.equals(y, flatData.y) && Objects.equals(creationDate, flatData.creationDate) && Objects.equals(area, flatData.area) && Objects.equals(balcony, flatData.balcony) && Objects.equals(furniture, flatData.furniture) && view == flatData.view && transport == flatData.transport && Objects.equals(houseId, flatData.houseId) && Objects.equals(houseName, flatData.houseName) && Objects.equals(houseYear, flatData.houseYear) && Objects.equals(houseNumberOfFloors, flatData.houseNumberOfFloors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinatesId, x, y, creationDate, area, price, balcony, timeToMetroOnFoot, numberOfRooms, furniture, view, transport, houseId, houseName, houseYear, houseNumberOfFloors);
    }
}
