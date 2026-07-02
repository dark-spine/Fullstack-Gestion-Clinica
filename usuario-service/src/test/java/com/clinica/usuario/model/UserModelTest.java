package com.clinica.usuario.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Model Tests")
class UserModelTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("john.doe")
                .email("john@example.com")
                .password("securePassword123")
                .role("DOCTOR")
                .profileType("SPECIALIST")
                .build();
    }

    @Test
    @DisplayName("Should create User with all fields")
    void testCreateUserWithAllFields() {
        // Act
        User testUser = new User(1L, "jane.smith", "jane@example.com", "password456", "PATIENT", "GENERAL");

        // Assert
        assertNotNull(testUser);
        assertEquals(1L, testUser.getId());
        assertEquals("jane.smith", testUser.getUsername());
        assertEquals("jane@example.com", testUser.getEmail());
        assertEquals("password456", testUser.getPassword());
        assertEquals("PATIENT", testUser.getRole());
        assertEquals("GENERAL", testUser.getProfileType());
    }

    @Test
    @DisplayName("Should set and get username")
    void testSetAndGetUsername() {
        // Arrange
        String newUsername = "updated.username";

        // Act
        user.setUsername(newUsername);

        // Assert
        assertEquals(newUsername, user.getUsername());
    }

    @Test
    @DisplayName("Should set and get email")
    void testSetAndGetEmail() {
        // Arrange
        String newEmail = "newemail@example.com";

        // Act
        user.setEmail(newEmail);

        // Assert
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    @DisplayName("Should set and get password")
    void testSetAndGetPassword() {
        // Arrange
        String newPassword = "newSecurePassword456";

        // Act
        user.setPassword(newPassword);

        // Assert
        assertEquals(newPassword, user.getPassword());
    }

    @Test
    @DisplayName("Should test equality between two identical users")
    void testEqualityBetweenIdenticalUsers() {
        // Arrange
        User user1 = User.builder()
                .id(1L)
                .username("john.doe")
                .email("john@example.com")
                .password("securePassword123")
                .role("DOCTOR")
                .profileType("SPECIALIST")
                .build();

        User user2 = User.builder()
                .id(1L)
                .username("john.doe")
                .email("john@example.com")
                .password("securePassword123")
                .role("DOCTOR")
                .profileType("SPECIALIST")
                .build();

        // Assert
        assertEquals(user1, user2);
    }

    @Test
    @DisplayName("Should test inequality between different users")
    void testInequalityBetweenDifferentUsers() {
        // Arrange
        User differentUser = User.builder()
                .id(2L)
                .username("different.user")
                .email("different@example.com")
                .password("password789")
                .role("PATIENT")
                .profileType("GENERAL")
                .build();

        // Assert
        assertNotEquals(user, differentUser);
    }

    @Test
    @DisplayName("Should verify all fields are non-null when using Builder")
    void testAllFieldsNonNullWithBuilder() {
        // Arrange
        User builtUser = User.builder()
                .id(5L)
                .username("test.user")
                .email("test@example.com")
                .password("testPassword")
                .role("DOCTOR")
                .profileType("SPECIALIST")
                .build();

        // Assert
        assertAll(
                () -> assertNotNull(builtUser.getId()),
                () -> assertNotNull(builtUser.getUsername()),
                () -> assertNotNull(builtUser.getEmail()),
                () -> assertNotNull(builtUser.getPassword()),
                () -> assertNotNull(builtUser.getRole()),
                () -> assertNotNull(builtUser.getProfileType())
        );
    }
}
