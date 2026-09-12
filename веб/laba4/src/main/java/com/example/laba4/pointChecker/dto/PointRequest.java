package com.example.laba4.pointChecker.dto;

import jakarta.validation.constraints.*;

public class PointRequest {
    @NotNull(message = "X не может быть null")
    private Double x;

    @NotNull(message = "Y не может быть null")
    private Double y;

    @NotNull(message = "R не может быть null")
    @Min(value = -3, message = "R должен быть >= -3")
    @Max(value = 5, message = "R должен быть <= 5")
    private Integer r;

    public PointRequest() {}

    public PointRequest(Double x, Double y, Integer r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Integer getR() {
        return r;
    }

    public void setR(Integer r) {
        this.r = r;
    }
}
