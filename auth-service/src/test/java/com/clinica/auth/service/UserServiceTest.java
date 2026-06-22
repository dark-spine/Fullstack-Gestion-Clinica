package com.clinica.auth.service;

import com.clinica.auth.model.User;
import com.clinica.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserRepository repo;
    private UserService userService;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(UserRepository.class);
        userService = new UserService(repo);
    }

    @Test
    void registerAndFind() {
        User saved = new User("alice", "$2a$10$hashed", "ROLE_USER");
        saved.setId(1L);
        when(repo.save(any(User.class))).thenReturn(saved);

        var result = userService.register("alice", "password", "ROLE_USER");
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("alice", result.getUsername());
    }

    @Test
    void validateCredentials_false_when_missing() {
        when(repo.findByUsername("bob")).thenReturn(Optional.empty());
        assertFalse(userService.validateCredentials("bob", "x"));
    }
}
