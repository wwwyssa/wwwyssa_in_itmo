package repository;

import dto.PageData;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import model.Coordinates;
import service.pagination.PageRequest;

@Stateless
public class CoordinatesRepository {
    @PersistenceContext(unitName = "flatsPU")
    private EntityManager entityManager;

    public Coordinates find(long id) {
        return entityManager.find(Coordinates.class, id);
    }

    public void persist(Coordinates coordinates) {
        entityManager.persist(coordinates);
    }

    public void flush() {
        entityManager.flush();
    }

    public PageData<Coordinates> list(PageRequest page) {
        page.validateSort(Set.of("id", "x", "y"));
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Coordinates> countRoot = countQuery.from(Coordinates.class);
        countQuery.select(builder.count(countRoot));
        long total = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<Coordinates> query = builder.createQuery(Coordinates.class);
        Root<Coordinates> root = query.from(Coordinates.class);
        query.select(root).orderBy(QuerySupport.sortOrders(builder, root, page));
        List<Coordinates> items = entityManager.createQuery(query)
                .setFirstResult(page.offset()).setMaxResults(page.size()).getResultList();
        return new PageData<>(items, page.page(), page.size(), total);
    }
}
