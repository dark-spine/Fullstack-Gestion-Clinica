package com.clinica.auth.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserModelTest {

    @Test
    void gettersAndSetters() {
        User u = new User("alice","secret","ROLE_USER");
        u.setId(5L);
        assertEquals(5L, u.getId());
        assertEquals("alice", u.getUsername());
        assertEquals("secret", u.getPassword());
        assertEquals("ROLE_USER", u.getRoles());

        u.setUsername("bob");
        u.setPassword("pwd");
        u.setRoles("ROLE_ADMIN");
        assertEquals("bob", u.getUsername());
        assertEquals("pwd", u.getPassword());
        assertEquals("ROLE_ADMIN", u.getRoles());
    }
}
