package com.wwwyssa.lab7.server;

import com.wwwyssa.lab7.server.dao.UserDAO;
import com.wwwyssa.lab7.server.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;


public class test {
    static SessionFactoryImpl sessionFactory = (SessionFactoryImpl) getHibernateSessionFactory();

    public static void main(String[] args) {
        var dao = new UserDAO();
        dao.setName("321");
        dao.setPassword("321");
        dao.setSalt("312");

        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(dao);
        session.getTransaction().commit();
        session.close();
    }
    private static SessionFactory getHibernateSessionFactory() {
        String url = "jdbc:postgresql://localhost:5432/laba7";
        String user = "postgres";
        String password = "123";
        return HibernateUtil.getSessionFactory(url, user, password);
    }
}
