package controller;


import model.Point;
import service.PointService;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import com.google.gson.Gson;

@Named("pointBean")
@SessionScoped
public class PointBean implements Serializable {

    @EJB
    private PointService pointService;

    private double x = 0;
    private double y;
    private double r = 3;

    private List<Point> results;
    private String jsonResults;

    @PostConstruct
    public void init() {
        loadResults();
    }

    public void checkArea() {
        try {
            Point newPoint = pointService.savePoint(x, y, r);
            results.add(0, newPoint);
            updateJsonResults();
        } catch (Exception e) {
            System.err.println("Ошибка при проверке точки из формы: " + e.getMessage());
        }
    }

    public void checkAreaFromCanvas() {
        checkArea();
    }

    private void loadResults() {
        results = pointService.getAllPoints();
        updateJsonResults();
    }

    private void updateJsonResults() {
        Gson gson = new Gson();
        this.jsonResults = gson.toJson(results);
    }

    public void filterPointsByR() {
        double selectedR = this.r;
        List<Point> filteredPoints = pointService.getAllPoints().stream()
                .filter(point -> point.getR() == selectedR)
                .toList();
        this.results = filteredPoints;
        updateJsonResults(); // Update JSON for the graph
    }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getR() { return r; }
    public void setR(double r) { this.r = r; }

    public List<Point> getResults() { return results; }
    public String getJsonResults() { return jsonResults; }
}