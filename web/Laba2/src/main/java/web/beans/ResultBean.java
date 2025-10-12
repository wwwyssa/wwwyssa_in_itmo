package web.beans;

import web.models.Point;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        this.points.add(point);
    }


}
