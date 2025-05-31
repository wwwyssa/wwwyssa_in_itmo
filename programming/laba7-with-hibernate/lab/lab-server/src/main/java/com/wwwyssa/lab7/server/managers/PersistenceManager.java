package com.wwwyssa.lab7.server.managers;

import com.wwwyssa.lab7.common.models.Organization;
import com.wwwyssa.lab7.common.models.Product;
import com.wwwyssa.lab7.common.util.User;
import com.wwwyssa.lab7.common.util.executions.AnswerString;
import com.wwwyssa.lab7.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab7.server.Server;
import com.wwwyssa.lab7.server.dao.ProductDAO;
import com.wwwyssa.lab7.server.dao.UserDAO;
import org.hibernate.SessionFactory;
import com.wwwyssa.lab7.server.dao.OrganizationDAO;

import java.util.*;

public class PersistenceManager {
    private final SessionFactory sessionFactory;


    public PersistenceManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ExecutionResponse add(User user, Product product) {
        Server.logger.info("Добавление нового продукта " + product.getName());

        OrganizationDAO newOrg = null;
        if (product.getManufacturer() != null) {
            newOrg = addOrganization(user, product.getManufacturer());
        }


        ProductDAO dao = new ProductDAO(product);
        dao.setManufacturer(newOrg);
        dao.setCreator(new UserDAO(user));

        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(dao);
        session.getTransaction().commit();
        session.close();

        Server.logger.info("Добавление продукта успешно выполнено.");

        long newId = dao.getId();
        Server.logger.info("Новый id продукта это " + newId);
        return new ExecutionResponse(true, new AnswerString(String.valueOf(newId)));
    }

    public OrganizationDAO addOrganization(User user, Organization organization) {
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        UserDAO userDAO = session.get(UserDAO.class, user.getId());
        if (userDAO == null) {
            throw new IllegalArgumentException("Пользователь с id=" + user.getId() + " не найден!");
        }

        // 2. Создаём организацию и привязываем к пользователю
        OrganizationDAO orgDAO = new OrganizationDAO(organization);
        orgDAO.setCreator(userDAO);  // Устанавливаем владельца

        session.persist(orgDAO);
        session.getTransaction().commit();

        return orgDAO;
    }

    public void update(User user, Product product) {
        Server.logger.info("Обновление продукта id#" + product.getId());
        var session = sessionFactory.getCurrentSession();

        session.beginTransaction();
        var productDAO = session.get(ProductDAO.class, product.getId());

        if (product.getManufacturer() != null) {
            updateOrganization(user, product.getManufacturer());
        }
        productDAO.update(product);
        session.update(productDAO);

        session.getTransaction().commit();
        session.close();
        Server.logger.info("Обновление продукта выполнено!");
    }

    public void updateOrganization(User user, Organization organization) {
        Server.logger.info("Обновление организации id#" + organization.getId());

        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        var organizationDAO = session.get(OrganizationDAO.class, organization.getId());
        organizationDAO.setCreator(session.get(UserDAO.class, user.getId()));
        organizationDAO.update(organization);

        session.update(organizationDAO);
        session.getTransaction().commit();
        session.close();
        Server.logger.info("Обновление организации выполнено!");
    }

    public void clear(User user) {
        Server.logger.info("Очищение продуктов пользователя id#" + user.getId() + " из базы данных.");

        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        var query = session.createQuery("DELETE FROM product WHERE creator.id = :creator");
        query.setParameter("creator", user.getId());
        var deletedSize = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        Server.logger.info("Удалено " + deletedSize + " продуктов.");
    }

    public int remove(User user, long id) {
        Server.logger.info("Удаление продукта №" + id + " пользователя id#" + user.getId() + ".");

        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        var query = session.createQuery("DELETE FROM product WHERE creator.id = :creator AND id = :id");
        query.setParameter("creator", user.getId());
        query.setParameter("id", id);

        var deletedSize = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        Server.logger.info("Удалено " + deletedSize + " продуктов.");

        return deletedSize;
    }

    public List<ProductDAO> loadProducts() {
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        var cq = session.getCriteriaBuilder().createQuery(ProductDAO.class);
        var rootEntry = cq.from(ProductDAO.class);
        var all = cq.select(rootEntry);

        var result = session.createQuery(all).getResultList();
        session.getTransaction().commit();
        session.close();
        return result;
    }
}
