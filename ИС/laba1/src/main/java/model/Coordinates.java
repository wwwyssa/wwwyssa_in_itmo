package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "coordinates")
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Positive
    private Long id;

    @NotNull
    @Column(name = "x", nullable = false)
    private Float x;

    @NotNull
    @Column(name = "y", nullable = false)
    private Float y;

    public Coordinates() {
    }

    public Long getId() {
        return id;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    @AssertTrue(message = "Y не должен превышать 249")
    public boolean isYWithinRange() {
        return y == null || y <= 249f;
    }
}
