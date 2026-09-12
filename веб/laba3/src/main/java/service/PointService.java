package service;

import model.Point;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

@Stateless
public class PointService implements Serializable {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    private boolean checkHit(double x, double y, double r) {
        if (x >= 0 && y >= 0) {
            return (x * x + y * y) <= (r / 2.0) * (r / 2.0);
        }
        if (x < 0 && y > 0) {
            return false;
        }
        if (x <= 0 && y <= 0) {
            boolean cond1 = x >= -r / 2.0;
            boolean cond2 = y >= -r;
            boolean cond3 = y >= (-2.0 * x - r);
            return cond1 && cond2 && cond3;
        }
        if (x >= 0 && y <= 0) {
            boolean cond1 = x <= r;
            boolean cond2 = y >= -r / 2.0;
            return cond1 && cond2;
        }

        return false;
    }

    public Point savePoint(double x, double y, double r) {
        try {
            double roundedX = Math.round(x * 1000.0) / 1000.0;
            double roundedY = Math.round(y * 1000.0) / 1000.0;
            double roundedR = Math.round(r * 1000.0) / 1000.0;

            boolean isHit = checkHit(roundedX, roundedY, roundedR);
            Point newPoint = new Point(roundedX, roundedY, roundedR, isHit);

            em.persist(newPoint);
            return newPoint;
        } catch (Exception e) {
                System.err.println("Ошибка при сохранении точки: " + e.getMessage());
                e.printStackTrace();
                throw new EJBException("Failed to save point", e);
            }

    }

    public List<Point> getAllPoints() {
        CriteriaQuery<Point> cq = em.getCriteriaBuilder().createQuery(Point.class);
        cq.select(cq.from(Point.class)).orderBy(em.getCriteriaBuilder().desc(cq.from(Point.class).get("checkDate")));
        return em.createQuery(cq).getResultList();
    }
}