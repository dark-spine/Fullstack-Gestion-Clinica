package com.clinica.usuario.controller;

import com.clinica.usuario.dto.UserRequestDTO;
import com.clinica.usuario.dto.UserResponseDTO;
import com.clinica.usuario.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("User Controller Tests")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        userRequestDTO = UserRequestDTO.builder()
                .username("john.doe")
                .email("john@example.com")
                .password("securePassword123")
                .role("DOCTOR")
                .profileType("SPECIALIST")
                .build();

        userResponseDTO = UserResponseDTO.builder()
                .id(1L)
                .username("john.doe")
                .email("john@example.com")
                .role("DOCTOR")
                .profileType("SPECIALIST")
                .build();
    }

    @Test
    @DisplayName("POST /api/users should return 201 Created")
    void testCreateUserReturns201() throws Exception {
        // Arrange
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("john.doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(jsonPath("$.role", is("DOCTOR")))
                .andExpect(jsonPath("$.profileType", is("SPECIALIST")));

        verify(userService, times(1)).createUser(any(UserRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/users should return 400 Bad Request with invalid data")
    void testCreateUserReturns400WithInvalidData() throws Exception {
        // Arrange
        UserRequestDTO invalidRequest = UserRequestDTO.builder()
                .username("")
                .email("invalid-email")
                .password("")
                .role("")
                .profileType("")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/users should return 200 OK with list of users")
    void testGetUsersReturns200() throws Exception {
        // Arrange
        UserResponseDTO user2 = UserResponseDTO.builder()
                .id(2L)
                .username("jane.smith")
                .email("jane@example.com")
                .role("PATIENT")
                .profileType("GENERAL")
                .build();

        List<UserResponseDTO> users = Arrays.asList(userResponseDTO, user2);
        when(userService.listUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("john.doe")))
                .andExpect(jsonPath("$[0].email", is("john@example.com")))
                .andExpect(jsonPath("$[1].username", is("jane.smith")))
                .andExpect(jsonPath("$[1].email", is("jane@example.com")));

        verify(userService, times(1)).listUsers();
    }

    @Test
    @DisplayName("GET /api/users should return 200 OK with empty list")
    void testGetUsersReturns200EmptyList() throws Exception {
        // Arrange
        when(userService.listUsers()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(userService, times(1)).listUsers();
    }

    @Test
    @DisplayName("POST /api/users should verify service is called with correct DTO")
    void testCreateUserCallsServiceWithCorrectDTO() throws Exception {
        // Arrange
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        // Act
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated());

        // Assert
        verify(userService).createUser(any(UserRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/users should return user response with all fields")
    void testCreateUserResponseContainsAllFields() throws Exception {
        // Arrange
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.role").exists())
                .andExpect(jsonPath("$.profileType").exists());
    }
}
