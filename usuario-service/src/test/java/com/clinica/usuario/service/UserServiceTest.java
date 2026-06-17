package com.clinica.usuario.service;

import com.clinica.usuario.dto.UserRequestDTO;
import com.clinica.usuario.dto.UserResponseDTO;
import com.clinica.usuario.mapper.UserMapper;
import com.clinica.usuario.model.User;
import com.clinica.usuario.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserRequestDTO userRequestDTO;
    private User user;
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

        user = User.builder()
                .id(1L)
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
    @DisplayName("Should successfully create a new user")
    void testCreateUserSuccess() {
        // Arrange
        when(userMapper.toEntity(userRequestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.createUser(userRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("john.doe", result.getUsername());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("DOCTOR", result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should list all users successfully")
    void testListUsersSuccess() {
        // Arrange
        User user2 = User.builder()
                .id(2L)
                .username("jane.smith")
                .email("jane@example.com")
                .password("password456")
                .role("PATIENT")
                .profileType("GENERAL")
                .build();

        UserResponseDTO responseDTO2 = UserResponseDTO.builder()
                .id(2L)
                .username("jane.smith")
                .email("jane@example.com")
                .role("PATIENT")
                .profileType("GENERAL")
                .build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));
        when(userMapper.toResponse(user)).thenReturn(userResponseDTO);
        when(userMapper.toResponse(user2)).thenReturn(responseDTO2);

        // Act
        List<UserResponseDTO> result = userService.listUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("john.doe", result.get(0).getUsername());
        assertEquals("jane.smith", result.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should list users returning empty list when no users exist")
    void testListUsersEmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<UserResponseDTO> result = userService.listUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should verify mapper is called during user creation")
    void testMapperCalledDuringCreation() {
        // Arrange
        when(userMapper.toEntity(userRequestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponseDTO);

        // Act
        userService.createUser(userRequestDTO);

        // Assert
        verify(userMapper, times(1)).toEntity(userRequestDTO);
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    @DisplayName("Should verify repository save is called with correct entity")
    void testRepositorySaveCalledWithCorrectEntity() {
        // Arrange
        when(userMapper.toEntity(userRequestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponseDTO);

        // Act
        userService.createUser(userRequestDTO);

        // Assert
        verify(userRepository, times(1)).save(user);
    }
}
