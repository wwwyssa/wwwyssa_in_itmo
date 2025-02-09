package models;
import utils.Validatable;

public class Coordinates implements Validatable {
    private Integer x; //Максимальное значение поля: 765, Поле не может быть null
    private long y; //Значение поля должно быть больше -395

    public Coordinates (Integer x, Long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isValid() {
        return x != null && x <= 765 && y > -395;
    }
}
