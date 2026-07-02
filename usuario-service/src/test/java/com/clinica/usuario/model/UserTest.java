package com.clinica.usuario.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test void testUserCreation() {
        User user = User.builder().id(1L).username("juan").email("juan@test.com").password("pass123")
            .role("ADMIN").profileType("DOCTOR").build();
        assertNotNull(user);
        assertEquals("juan", user.getUsername());
        assertEquals("juan@test.com", user.getEmail());
    }
    
    @Test void testUserEquality() {
        User user1 = User.builder().id(1L).username("ana").email("ana@test.com").password("pass").role("USER").profileType("PATIENT").build();
        User user2 = User.builder().id(1L).username("ana").email("ana@test.com").password("pass").role("USER").profileType("PATIENT").build();
        assertEquals(user1, user2);
    }
}
