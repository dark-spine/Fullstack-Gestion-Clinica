package com.clinica.citas.client;

import com.clinica.citas.dto.SlotAgendaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "agenda-client", url = "${agenda.service.url}")
public interface AgendaClient {
    @GetMapping("/api/agenda/{id}")
    SlotAgendaDTO getSlotById(@PathVariable("id") Long id);

    @PatchMapping("/api/agenda/{id}/estado?estado=RESERVADO")
    SlotAgendaDTO reservarSlot(@PathVariable("id") Long id);
}