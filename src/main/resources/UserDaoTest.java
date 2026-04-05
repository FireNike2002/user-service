import com.example.userservice.dao.UserDao;
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
        user.setUsername("Alice");
        user.setEmail("alice@example.com");

        userDao.create(user); // теперь userDao не null
        User fromDb = userDao.read(user.getId());
        Assertions.assertNotNull(fromDb);
        Assertions.assertEquals("Alice", fromDb.getUsername());
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setUsername("Bob");
        userDao.create(user);

        user.setUsername("Robert");
        userDao.update(user);

        User updated = userDao.read(user.getId());
        Assertions.assertEquals("Robert", updated.getUsername());
    }

    @Test
    void testDelete() {
        User user = new User();
        userDao.create(user);
        userDao.delete(user.getId());
        Assertions.assertNull(userDao.read(user.getId()));
    }
}