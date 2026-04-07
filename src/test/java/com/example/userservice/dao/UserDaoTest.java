package com.example.userservice.dao;

import com.example.userservice.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");

    private static SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeAll
    static void setup() {
        Configuration configuration = new Configuration();

        configuration.configure();

        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());

        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        sessionFactory = configuration.buildSessionFactory();
    }

    @BeforeEach
    void init() {
        userDao = new UserDao(sessionFactory);
    }

    @AfterAll
    static void tearDown() {
        sessionFactory.close();
    }

    @Test
    void testCreateAndRead() {
        User user = new User();
        user.setName("Mike");

        userDao.create(user);

        User found = userDao.read(user.getId());

        assertNotNull(found);
        assertEquals("Mike", found.getName());
    }
}