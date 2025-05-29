package com.wwwyssa.lab7.server.util;


import java.util.Properties;

import com.wwwyssa.lab7.server.Server;
import com.wwwyssa.lab7.server.dao.OrganizationDAO;
import com.wwwyssa.lab7.server.dao.ProductDAO;
import com.wwwyssa.lab7.server.dao.UserDAO;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class HibernateUtil {

    //Property based configuration
    private static SessionFactory sessionFactory;

    private static SessionFactory buildSessionFactory(String url, String user, String password) {
        try {

            Properties props = new Properties();
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            props.put("hibernate.connection.driver_class", "org.postgresql.Driver");

            props.put("hibernate.connection.username", user);
            props.put("hibernate.connection.password", password);
            props.put("hibernate.connection.url", url);

            props.put("hibernate.connection.pool_size", "100");
            props.put("hibernate.current_session_context_class", "thread");
            props.put("hibernate.connection.autocommit", "true");
            props.put("hibernate.show_sql", "true");
            props.put("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");
            props.put("hibernate.hbm2ddl.auto", "validate");

            Configuration configuration = new Configuration();
            configuration.setProperties(props);

            configuration.addPackage("server.dao");
            configuration.addAnnotatedClass(OrganizationDAO.class);
            configuration.addAnnotatedClass(ProductDAO.class);
            configuration.addAnnotatedClass(UserDAO.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            Server.logger.info("Hibernate service registry created successfully");
            return configuration.buildSessionFactory(serviceRegistry);
        }
        catch (Throwable ex) {
            Server.logger.info("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory(String url, String user, String password) {
        if(sessionFactory == null) sessionFactory = buildSessionFactory(url, user, password);
        return sessionFactory;
    }
}

