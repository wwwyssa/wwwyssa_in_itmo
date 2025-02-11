package models;

import utils.Validatable;

public class Address implements Validatable {
    private String street; //Поле не может быть null
    private Location town; //Поле может быть null

    @Override
    public boolean isValid() {
        return street != null && town != null;
    }

    public Address(String street, Location town) {
        this.street = street;
        this.town = town;
    }
}
