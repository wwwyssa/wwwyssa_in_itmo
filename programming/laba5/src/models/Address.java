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

    public String getStreet() {
        return street;
    }

    public Location getTown() {
        return town;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", town=" + town +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Address) {
            Address address = (Address) obj;
            return street.equals(address.street) && town.equals(address.town);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return street.hashCode() + town.hashCode();
    }


}
