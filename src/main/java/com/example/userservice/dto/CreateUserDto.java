package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CreateUserDto {
    @NotBlank(message = "Имя обязательно")
    private String name;

    @NotBlank(message = "Почта обязательна")
    @Email(message = "Почта должна быть корректна")
    private String email;

    @Positive(message = "Возраст должен быть положительным")
    private Integer age;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}