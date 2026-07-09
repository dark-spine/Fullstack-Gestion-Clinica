package com.clinica.auth.controller;

import com.clinica.auth.config.JwtUtil;
import com.clinica.auth.model.User;
import com.clinica.auth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {

    private UserService userService;
    private JwtUtil jwtUtil;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        jwtUtil = Mockito.mock(JwtUtil.class);
        AuthController controller = new AuthController(jwtUtil, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void registerReturns400WhenMissing() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void registerCreatesUserWhenNotExists() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());
        User u = new User("alice","hashed","ROLE_USER");
        u.setId(10L);
        when(userService.register(Mockito.eq("alice"), Mockito.anyString(), Mockito.eq("ROLE_USER"))).thenReturn(u);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"alice\",\"password\":\"pwd\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    void loginInvalidCredentialsReturns401() throws Exception {
        when(userService.validateCredentials(anyString(), anyString())).thenReturn(false);
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"alice\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("invalid credentials"));
    }

    @Test
    void loginReturnsTokenWhenValid() throws Exception {
        when(userService.validateCredentials(anyString(), anyString())).thenReturn(true);
        User u = new User("alice","hashed","ROLE_USER");
        when(userService.findByUsername("alice")).thenReturn(Optional.of(u));
        when(jwtUtil.generateToken("alice", "ROLE_USER")).thenReturn("tok-123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"alice\",\"password\":\"pwd\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("tok-123"));
    }
}
