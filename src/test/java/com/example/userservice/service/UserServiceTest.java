//package com.example.userservice.service;
//
//import com.example.userservice.dao.UserDao;
//import com.example.userservice.model.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class UserServiceTest {
//
//    private UserDao userDao;
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        userDao = Mockito.mock(UserDao.class);
//        userService = new UserService(userDao);
//    }
//
//    @Test
//    void testCreateUser() {
//        User user = new User("Test", "test@mail.com", 25);
//
//        userService.createUser(user);
//
//        verify(userDao, times(1)).create(user);
//    }
//
//    @Test
//    void testCreateUser_invalidEmail() {
//        User user = new User("Test", "", 25);
//
//        assertThrows(IllegalArgumentException.class,
//                () -> userService.createUser(user));
//    }
//
//    @Test
//    void testGetUser() {
//        User user = new User("Test", "test@mail.com", 25);
//        when(userDao.read(1L)).thenReturn(user);
//
//        User result = userService.getUser(1L);
//
//        assertEquals("Test", result.getName());
//        assertEquals("test@mail.com", result.getEmail());
//        assertEquals(25, result.getAge());
//    }
//
//    @Test
//    void testGetAllUsers() {
//        when(userDao.readAll()).thenReturn(List.of(
//                new User("A", "a@mail.com", 20),
//                new User("B", "b@mail.com", 30)
//        ));
//
//        List<User> users = userService.getAllUsers();
//
//        assertEquals(2, users.size());
//        assertEquals("A", users.get(0).getName());
//        assertEquals("B", users.get(1).getName());
//    }
//
//    @Test
//    void testDeleteUser() {
//        userService.deleteUser(1L);
//
//        verify(userDao).delete(1L);
//    }
//
//    @Test
//    void testUpdateUser() {
//        User user = new User("Updated", "updated@mail.com", 35);
//
//        userService.updateUser(user);
//
//        verify(userDao).update(user);
//    }
//}