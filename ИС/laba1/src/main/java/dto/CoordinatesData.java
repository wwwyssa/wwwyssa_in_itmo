package dto;

import model.Coordinates;

public class CoordinatesData {
    public Long id;
    public Float x;
    public Float y;

    public CoordinatesData(Coordinates coordinates) {
        id = coordinates.getId();
        x = coordinates.getX();
        y = coordinates.getY();
    }
}
