package dto;

import model.House;

public class HouseData {
    public Long id;
    public String name;
    public Long year;
    public int numberOfFloors;

    public HouseData(House house) {
        id = house.getId();
        name = house.getName();
        year = house.getYear();
        numberOfFloors = house.getNumberOfFloors();
    }
}
