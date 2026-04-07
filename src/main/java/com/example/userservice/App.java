package com.example.userservice;

import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;
import com.example.userservice.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Scanner;

public class App {

    private static final UserDao userDao = new UserDao(HibernateUtil.getSessionFactory());
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- User Service ---");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Список пользователей");
            System.out.println("3. Найти пользователя по ID");
            System.out.println("4. Обновить пользователя");
            System.out.println("5. Удалить пользователя");
            System.out.println("0. Выход");
            System.out.print("Выберите операцию: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createUser();
                case "2" -> listUsers();
                case "3" -> getUserById();
                case "4" -> updateUser();
                case "5" -> deleteUser();
                case "0" -> {
                    HibernateUtil.shutdown();
                    System.exit(0);
                }
                default -> System.out.println("неверный выбор");
            }
        }
    }

    private static void createUser() {
        System.out.print("Имя: ");
        String name = scanner.nextLine();
        System.out.print("Почта: ");
        String email = scanner.nextLine();
        System.out.print("Возраст: ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = new User(name, email, age);
        userDao.create(user);
        System.out.println("Пользователь создан: " + user);
    }

    private static void listUsers() {
        List<User> users = userDao.readAll();
        if (users != null) {
            users.forEach(System.out::println);
        }
    }

    private static void getUserById() {
        System.out.print("ID пользователя: ");
        long id = Long.parseLong(scanner.nextLine());
        User user = userDao.read(id);
        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println("Не найден");
        }
    }

    private static void updateUser() {
        System.out.print("Пользователь: ");
        long id = Long.parseLong(scanner.nextLine());
        User user = userDao.read(id);
        if (user == null) {
            System.out.println("Не найден");
            return;
        }

        System.out.print("Новое имя (" + user.getName() + "): ");
        String name = scanner.nextLine();
        System.out.print("Новая почта (" + user.getEmail() + "): ");
        String email = scanner.nextLine();
        System.out.print("Новый возраст (" + user.getAge() + "): ");
        String ageStr = scanner.nextLine();

        if (!name.isEmpty()) user.setName(name);
        if (!email.isEmpty()) user.setEmail(email);
        if (!ageStr.isEmpty()) user.setAge(Integer.parseInt(ageStr));

        userDao.update(user);
        System.out.println("Обновлен: " + user);
    }

    private static void deleteUser() {
        System.out.print("Введите номер пользователя: ");
        long id = Long.parseLong(scanner.nextLine());
        userDao.delete(id);
        System.out.println("Удален");
    }
}