package com.clinica.notificaciones.controller;

import com.clinica.notificaciones.model.Notification;
import com.clinica.notificaciones.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationRepository repository;

    @GetMapping
    public List<Notification> getAll() {
        return repository.findAll();
    }
}
