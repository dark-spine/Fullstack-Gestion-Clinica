package com.clinica.auth.service;

import com.clinica.auth.model.User;
import com.clinica.auth.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    public User register(String username, String rawPassword, String roles) {
        String hashed = encoder.encode(rawPassword);
        User u = new User(username, hashed, roles);
        return userRepository.save(u);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean validateCredentials(String username, String rawPassword) {
        Optional<User> u = userRepository.findByUsername(username);
        return u.map(user -> encoder.matches(rawPassword, user.getPassword())).orElse(false);
    }
}
