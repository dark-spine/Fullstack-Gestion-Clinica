package com.clinica.usuario.repository;

import com.clinica.usuario.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("User Repository Tests")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("john.doe")
                .email("john@example.com")
                .password("securePassword123")
                .role("DOCTOR")
                .profileType("SPECIALIST")
                .build();
    }

    @Test
    @DisplayName("Should save user successfully")
    void testSaveUserSuccess() {
        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertNotNull(savedUser.getId());
        assertEquals("john.doe", savedUser.getUsername());
        assertEquals("john@example.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("Should find user by username")
    void testFindByUsername() {
        // Arrange
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByUsername("john.doe");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("john.doe", foundUser.get().getUsername());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty Optional when username not found")
    void testFindByUsernameNotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("nonexistent.user");

        // Assert
        assertTrue(foundUser.isEmpty());
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindByEmail() {
        // Arrange
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("john@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("john@example.com", foundUser.get().getEmail());
        assertEquals("john.doe", foundUser.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty Optional when email not found")
    void testFindByEmailNotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertTrue(foundUser.isEmpty());
    }

    @Test
    @DisplayName("Should find user by id")
    void testFindById() {
        // Arrange
        User savedUser = userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals("john.doe", foundUser.get().getUsername());
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUserSuccess() {
        // Arrange
        User savedUser = userRepository.save(user);
        savedUser.setEmail("newemail@example.com");
        savedUser.setRole("PATIENT");

        // Act
        User updatedUser = userRepository.save(savedUser);

        // Assert
        assertEquals("newemail@example.com", updatedUser.getEmail());
        assertEquals("PATIENT", updatedUser.getRole());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUserSuccess() {
        // Arrange
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        // Act
        userRepository.deleteById(userId);

        // Assert
        Optional<User> deletedUser = userRepository.findById(userId);
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    @DisplayName("Should count all users")
    void testCountAllUsers() {
        // Arrange
        User user2 = User.builder()
                .username("jane.smith")
                .email("jane@example.com")
                .password("password456")
                .role("PATIENT")
                .profileType("GENERAL")
                .build();
        userRepository.save(user);
        userRepository.save(user2);

        // Act
        long count = userRepository.count();

        // Assert
        assertEquals(2, count);
    }
}
