package dto;

import model.Transport;
import model.View;

public class FlatRequest {
    public String name;
    public Long coordinatesId;
    public Float x;
    public Float y;
    public Long area;
    public Double price;
    public Boolean balcony;
    public Float timeToMetroOnFoot;
    public Integer numberOfRooms;
    public Boolean furniture;
    public View view;
    public Transport transport;
    public Long houseId;
}
