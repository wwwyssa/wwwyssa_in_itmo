package repository;

import dto.PageData;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import model.House;
import service.pagination.PageRequest;

@Stateless
public class HouseRepository {
    @PersistenceContext(unitName = "flatsPU")
    private EntityManager entityManager;

    public House find(long id) {
        return entityManager.find(House.class, id);
    }

    public void persist(House house) {
        entityManager.persist(house);
    }

    public void flush() {
        entityManager.flush();
    }

    public PageData<House> list(PageRequest page, String name) {
        page.validateSort(Set.of("id", "name", "year", "numberOfFloors"));
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<House> countRoot = countQuery.from(House.class);
        countQuery.select(builder.count(countRoot)).where(nameFilter(builder, countRoot, name));
        long total = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<House> query = builder.createQuery(House.class);
        Root<House> root = query.from(House.class);
        query.select(root).where(nameFilter(builder, root, name))
                .orderBy(QuerySupport.sortOrders(builder, root, page));
        List<House> items = entityManager.createQuery(query)
                .setFirstResult(page.offset()).setMaxResults(page.size()).getResultList();
        return new PageData<>(items, page.page(), page.size(), total);
    }

    private Predicate nameFilter(CriteriaBuilder builder, Root<House> root, String name) {
        if (name == null || name.isEmpty()) {
            return builder.conjunction();
        }
        return builder.like(builder.lower(root.get("name")), QuerySupport.containsPattern(name), '\\');
    }
}
