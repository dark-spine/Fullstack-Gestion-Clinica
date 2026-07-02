package com.clinica.auth;

import com.clinica.auth.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;

    public DataLoader(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        // create default admin if not present
        userService.findByUsername("admin").orElseGet(() -> userService.register("admin", "admin123", "ROLE_ADMIN"));
    }
}
