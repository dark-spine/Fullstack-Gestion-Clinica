package com.clinica.auth.repository;

import com.clinica.auth.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void saveFindByIdFindAllAndFindByUsername() {
        User u = new User("alice","pwd","ROLE_USER");
        User saved = repository.save(u);
        assertNotNull(saved.getId());

        var found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("alice", found.get().getUsername());

        List<User> all = repository.findAll();
        assertTrue(all.size() >= 1);

        var byUser = repository.findByUsername("alice");
        assertTrue(byUser.isPresent());
        assertEquals(saved.getId(), byUser.get().getId());
    }
}
