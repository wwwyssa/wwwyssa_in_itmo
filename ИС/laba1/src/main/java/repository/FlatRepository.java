package repository;

import dto.PageData;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import model.Flat;
import model.House;
import service.flat.FlatSearch;
import service.pagination.PageRequest;

@Stateless
public class FlatRepository {
    private static final Set<String> SORT_FIELDS = Set.of("id", "name", "coordinatesId", "x", "y",
            "creationDate", "area", "price", "balcony", "timeToMetroOnFoot", "numberOfRooms",
            "furniture", "view", "transport", "houseId", "houseName", "houseYear", "houseNumberOfFloors");

    @PersistenceContext(unitName = "flatsPU")
    private EntityManager entityManager;

    public Flat find(int id) {
        return entityManager.find(Flat.class, id);
    }

    public void persist(Flat flat) {
        entityManager.persist(flat);
    }

    public void remove(Flat flat) {
        entityManager.remove(flat);
    }

    public void flush() {
        entityManager.flush();
    }

    public PageData<Flat> list(FlatSearch search) {
        PageRequest page = search.pagination();
        page.validateSort(SORT_FIELDS);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Flat> countRoot = countQuery.from(Flat.class);
        Join<Flat, House> countHouse = countRoot.join("house", JoinType.LEFT);
        countQuery.select(builder.count(countRoot)).where(filters(builder, countRoot, countHouse, search));
        long total = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<Flat> query = builder.createQuery(Flat.class);
        Root<Flat> root = query.from(Flat.class);
        root.fetch("coordinates", JoinType.INNER);
        root.fetch("house", JoinType.LEFT);
        Join<Flat, House> house = root.join("house", JoinType.LEFT);
        Expression<?> field = sortField(root, house, page.sort());
        List<Order> orders = new ArrayList<>();
        orders.add(page.direction().equals("asc") ? builder.asc(field) : builder.desc(field));
        if (!page.sort().equals("id")) {
            orders.add(builder.asc(root.get("id")));
        }
        query.select(root).where(filters(builder, root, house, search)).orderBy(orders);
        List<Flat> items = entityManager.createQuery(query)
                .setFirstResult(page.offset()).setMaxResults(page.size()).getResultList();
        return new PageData<>(items, page.page(), page.size(), total);
    }

    private Predicate[] filters(CriteriaBuilder builder, Root<Flat> root,
                                Join<Flat, House> house, FlatSearch search) {
        List<Predicate> predicates = new ArrayList<>();
        contains(builder, predicates, root.get("name"), search.name());
        contains(builder, predicates, house.get("name"), search.houseName());
        if (search.numberOfRooms() != null) {
            predicates.add(builder.equal(root.get("numberOfRooms"), search.numberOfRooms()));
        }
        if (search.timeToMetroOnFoot() != null) {
            predicates.add(builder.equal(root.get("timeToMetroOnFoot"), search.timeToMetroOnFoot()));
        }
        return predicates.toArray(Predicate[]::new);
    }

    private void contains(CriteriaBuilder builder, List<Predicate> predicates,
                          Expression<String> field, String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(builder.like(builder.lower(field), QuerySupport.containsPattern(value), '\\'));
        }
    }

    private Expression<?> sortField(Root<Flat> root, Join<Flat, House> house, String field) {
        return switch (field) {
            case "coordinatesId" -> root.get("coordinates").get("id");
            case "x", "y" -> root.get("coordinates").get(field);
            case "houseId" -> house.get("id");
            case "houseName" -> house.get("name");
            case "houseYear" -> house.get("year");
            case "houseNumberOfFloors" -> house.get("numberOfFloors");
            default -> root.get(field);
        };
    }

    public Flat maxArea() {
        List<Flat> results = entityManager
            .createNativeQuery("SELECT * FROM flat_max_area()", Flat.class)
            .getResultList();

        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }

    public long countByHouse(Long houseId) {
        Number count = (Number) entityManager.createNativeQuery("SELECT flat_count_by_house(CAST(? AS BIGINT))")
                .setParameter(1, houseId).getSingleResult();
        return count.longValue();
    }

    public List<Flat> namesStartingWith(String prefix) {
        return function("SELECT * FROM flat_names_starting_with(?)", prefix);
    }

    public Flat mostExpensiveWithoutBalcony() {
        return singleFunction("SELECT * FROM flat_most_expensive_without_balcony()");
    }

    public Flat mostExpensiveOfThree(int id1, int id2, int id3) {
        return singleFunction("SELECT * FROM flat_most_expensive_of_three(?, ?, ?)", id1, id2, id3);
    }

    private Flat singleFunction(String sql, Object... parameters) {
        List<Flat> results = function(sql, parameters);
        return results.isEmpty() ? null : results.get(0);
    }

    private List<Flat> function(String sql, Object... parameters) {
        Query query = entityManager.createNativeQuery(sql, Flat.class);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i + 1, parameters[i]);
        }
        return query.getResultList();
    }
}
