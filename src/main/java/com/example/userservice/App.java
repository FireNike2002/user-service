package com.example.userservice;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) {
        while (true) {
            System.out.println("\nUser Service");
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
                case "0" -> System.exit(0);
                default -> System.out.println("incorrect");
            }
        }
    }

    private void createUser() {
        System.out.print("Имя: ");
        String name = scanner.nextLine();
        System.out.print("Почта: ");
        String email = scanner.nextLine();
        System.out.print("Возраст: ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = new User(name, email, age);
        User saved = userRepository.save(user);
        System.out.println("Пользователь создан: " + saved);
    }

    private void listUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
    }

    private void getUserById() {
        System.out.print("ID пользователя: ");
        long id = Long.parseLong(scanner.nextLine());
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            System.out.println(user.get());
        } else {
            System.out.println("Не найден");
        }
    }

    private void updateUser() {
        System.out.print("ID пользователя: ");
        long id = Long.parseLong(scanner.nextLine());
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            System.out.println("Не найден");
            return;
        }
        User user = optionalUser.get();

        System.out.print("Новое имя (" + user.getName() + "): ");
        String name = scanner.nextLine();
        System.out.print("Новая почта (" + user.getEmail() + "): ");
        String email = scanner.nextLine();
        System.out.print("Новый возраст (" + user.getAge() + "): ");
        String ageStr = scanner.nextLine();

        if (!name.isEmpty()) user.setName(name);
        if (!email.isEmpty()) user.setEmail(email);
        if (!ageStr.isEmpty()) user.setAge(Integer.parseInt(ageStr));

        userRepository.save(user);
        System.out.println("Обновлен: " + user);
    }

    private void deleteUser() {
        System.out.print("ID пользователя: ");
        long id = Long.parseLong(scanner.nextLine());
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            System.out.println("Удален");
        } else {
            System.out.println("Не найден");
        }
    }
}