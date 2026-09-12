package web.beans;

import web.models.Point;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResultBean implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<Point> points;

    public ResultBean() {
        this.points = new ArrayList<>();
    }

    public List<Point> getPoints() {
        return points;
    }
    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void addPoint(Point point) {
        List<Point> tmp = new ArrayList<>();
        tmp.add(point);
        tmp.addAll(points);
        this.points = tmp;
    }


    @Override
    public String toString() {
        return "ResultBean{" +
                "points=" + points +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultBean that = (ResultBean) o;

        return Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return points != null ? points.hashCode() : 0;
    }

}
