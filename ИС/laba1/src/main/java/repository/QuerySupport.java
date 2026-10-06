package repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import service.pagination.PageRequest;

public final class QuerySupport {
    private QuerySupport() {
    }

    public static String containsPattern(String text) {
        return "%" + text.toLowerCase(Locale.ROOT) + "%";
    }

    public static List<Order> sortOrders(CriteriaBuilder builder, Root<?> root, PageRequest page) {
        Expression<?> field = root.get(page.sort());
        List<Order> orders = new ArrayList<>();
        if (page.direction().equals("asc")) {
            orders.add(builder.asc(field));
        } else {
            orders.add(builder.desc(field));
        }
        if (!page.sort().equals("id")) {
            orders.add(builder.asc(root.get("id")));
        }
        return orders;
    }
}
