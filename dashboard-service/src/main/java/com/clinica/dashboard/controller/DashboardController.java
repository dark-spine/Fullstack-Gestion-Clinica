package com.clinica.dashboard.controller;

import com.clinica.dashboard.dto.DashboardResponseDTO;
import com.clinica.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService service;

    @GetMapping
    public DashboardResponseDTO getDashboard() {
        return service.getDashboard();
    }
}
