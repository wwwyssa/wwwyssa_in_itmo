package web.models;

import java.io.Serializable;
import java.util.Date;

public class Point implements Serializable {
    private double x;
    private double y;
    private double r;
    private boolean inside;
    private Date timestamp;
    private long executionTime;

    public Point() {}

    public Point(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.timestamp = new Date();
        this.inside = checkInside(x, y, r); // Автоматически проверяем при создании
    }

    // Геттеры и сеттеры
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getR() { return r; }
    public void setR(double r) { this.r = r; }

    public boolean isInside() { return inside; }
    public void setInside(boolean inside) { this.inside = inside; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public long getExecutionTime() { return executionTime; }
    public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }

    // Метод для проверки попадания
    public boolean checkInside(double x, double y, double r) {
        // Проверка прямоугольника (2 четверть)
        if (x >= -r && x <= 0 && y >= 0 && y <= r/2) {
            return true;
        }
        // Проверка треугольника (4 четверть)
        if (x >= 0 && y <= 0 && y >= -r/2 && x <= r/2 && (x + Math.abs(y)) <= r/2) {
            return true;
        }
        // Проверка четверти круга (3 четверть)
        if (x <= 0 && y <= 0 && (x*x + y*y) <= (r/2)*(r/2)) {
            return true;
        }
        return false;
    }
}