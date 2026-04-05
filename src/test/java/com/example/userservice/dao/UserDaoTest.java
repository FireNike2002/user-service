package com.example.userservice.dao;import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;
import org.junit.jupiter.api.*;

class UserDaoTest {

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
    }

    @Test
    void testCreateAndRead() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");

        userDao.create(user);
        User fromDb = userDao.read(user.getId());
        Assertions.assertNotNull(fromDb);
        Assertions.assertEquals("Alice", fromDb.getName());
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setName("Bob");
        userDao.create(user);

        user.setName("Robert");
        userDao.update(user);

        User updated = userDao.read(user.getId());
        Assertions.assertEquals("Robert", updated.getName());
    }

    @Test
    void testDelete() {
        User user = new User();
        userDao.create(user);
        userDao.delete(user.getId());
        Assertions.assertNull(userDao.read(user.getId()));
    }
}