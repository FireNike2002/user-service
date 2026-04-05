package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;

import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Почта не может быть пуста");
        }
        userDao.create(user);
    }

    public User getUser(Long id) {
        return userDao.read(id);
    }

    public List<User> getAllUsers() {
        return userDao.readAll();
    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }
}