package com.clinica.notificaciones.service;

import com.clinica.notificaciones.model.Notification;
import com.clinica.notificaciones.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;

    @Transactional
    public void sendNotification(String target, String channel, String message) {
        Notification notification = Notification.builder()
                .target(target)
                .channel(channel)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(notification);
        System.out.println("[Notification] channel=" + channel + " target=" + target + " message=" + message);
    }
}
