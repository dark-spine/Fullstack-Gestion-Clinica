package com.clinica.notificaciones.mapper;

import com.clinica.notificaciones.dto.NotificationResponseDTO;
import com.clinica.notificaciones.model.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponseDTO toResponse(Notification entity);
}
