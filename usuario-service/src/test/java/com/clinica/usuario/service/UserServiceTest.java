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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername("john.doe");
        userRequestDTO.setEmail("john@example.com");
        userRequestDTO.setPassword("password123");
        userRequestDTO.setRole("USER");

        user = new User();
        user.setId(1L);
        user.setUsername("john.doe");
        user.setEmail("john@example.com");

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setUsername("john.doe");
    }

    @Test
    @DisplayName("Should successfully create a new user")
    void testCreateUserSuccess() {
        when(userMapper.toEntity(userRequestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.createUser(userRequestDTO);

        assertNotNull(result);
        assertEquals("john.doe", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should list all users successfully")
    void testListUsersSuccess() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("jane.smith");

        UserResponseDTO responseDTO2 = new UserResponseDTO();
        responseDTO2.setId(2L);
        responseDTO2.setUsername("jane.smith");

        when(userRepository.findAll()).thenReturn(List.of(user, user2));
        when(userMapper.toResponse(user)).thenReturn(userResponseDTO);
        when(userMapper.toResponse(user2)).thenReturn(responseDTO2);

        List<UserResponseDTO> result = userService.listUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("john.doe", result.get(0).getUsername());
        assertEquals("jane.smith", result.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should list users returning empty list when no users exist")
    void testListUsersEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserResponseDTO> result = userService.listUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should verify mapper is called during user creation")
    void testMapperCalledDuringCreation() {
        when(userMapper.toEntity(userRequestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponseDTO);

        userService.createUser(userRequestDTO);

        verify(userMapper, times(1)).toEntity(userRequestDTO);
        verify(userMapper, times(1)).toResponse(user);
    }
}
