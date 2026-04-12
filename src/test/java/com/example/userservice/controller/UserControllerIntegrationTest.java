package com.example.userservice.controller;

import com.example.userservice.dto.CreateUserDto;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void createAndGetUser() throws Exception {
        CreateUserDto createDto = new CreateUserDto();
        createDto.setName("Integration");
        createDto.setEmail("int@test.com");
        createDto.setAge(30);

        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Integration")));
    }

    @Test
    void updateUser() throws Exception {
        User saved = userRepository.save(new User("Old", "old@mail.com", 10));

        String updateJson = "{ \"name\": \"New\", \"email\": \"new@mail.com\", \"age\": 20 }";
        mockMvc.perform(put("/api/users/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New")))
                .andExpect(jsonPath("$.email", is("new@mail.com")))
                .andExpect(jsonPath("$.age", is(20)));
    }

    @Test
    void deleteUser() throws Exception {
        User saved = userRepository.save(new User("Del", "del@mail.com", 5));

        mockMvc.perform(delete("/api/users/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }
}