package com.example.userservice.controller;

import com.example.userservice.dto.CreateUserDto;
import com.example.userservice.dto.UpdateUserDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        UserResponseDto user1 = new UserResponseDto(1L, "Alice", "alice@ex.com", 25, LocalDateTime.now());
        UserResponseDto user2 = new UserResponseDto(2L, "Bob", "bob@ex.com", 30, LocalDateTime.now());
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Alice")))
                .andExpect(jsonPath("$[1].email", is("bob@ex.com")));
    }

    @Test
    void getUserById_existing_returnsUser() throws Exception {
        UserResponseDto user = new UserResponseDto(1L, "Alice", "alice@ex.com", 25, LocalDateTime.now());
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Alice")));
    }

    @Test
    void getUserById_notFound_returns404() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_valid_returnsCreated() throws Exception {
        CreateUserDto createDto = new CreateUserDto();
        createDto.setName("Charlie");
        createDto.setEmail("charlie@ex.com");
        createDto.setAge(28);

        UserResponseDto response = new UserResponseDto(3L, "Charlie", "charlie@ex.com", 28, LocalDateTime.now());
        when(userService.createUser(any(CreateUserDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Charlie")));
    }

    @Test
    void createUser_invalidEmail_returnsBadRequest() throws Exception {
        CreateUserDto createDto = new CreateUserDto();
        createDto.setName("Charlie");
        createDto.setEmail("not-an-email");
        createDto.setAge(28);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_valid_returnsUpdated() throws Exception {
        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setName("Charlie Updated");
        updateDto.setEmail("charlie.new@ex.com");

        UserResponseDto updated = new UserResponseDto(3L, "Charlie Updated", "charlie.new@ex.com", 28, LocalDateTime.now());
        when(userService.updateUser(eq(3L), any(UpdateUserDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/users/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Charlie Updated")));
    }

    @Test
    void deleteUser_existing_returnsNoContent() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}