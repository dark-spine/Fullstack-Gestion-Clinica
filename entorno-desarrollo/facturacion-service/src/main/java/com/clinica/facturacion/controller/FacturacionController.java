package com.clinica.facturacion.controller;

import com.clinica.facturacion.dto.FacturaCreateDTO;
import com.clinica.facturacion.dto.FacturaDTO;
import com.clinica.facturacion.service.FacturaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService service;

    public FacturaController(FacturaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FacturaDTO> crear(@Valid @RequestBody FacturaCreateDTO request) {
        FacturaDTO nueva = service.crearFactura(request);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<FacturaDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.listarPorPaciente(pacienteId));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<FacturaDTO>> listarPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(service.listarPorMedico(medicoId));
    }

    @PatchMapping("/{id}/pagada")
    public ResponseEntity<FacturaDTO> marcarPagada(@PathVariable Long id,
                                                    @RequestParam(required = false) String fechaPago) {
        return ResponseEntity.ok(service.marcarPagada(id, fechaPago));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<FacturaDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelarFactura(id));
    }

    @GetMapping("/ingresos/medico/{medicoId}")
    public ResponseEntity<Double> ingresosPorMedico(@PathVariable Long medicoId,
                                                     @RequestParam String inicio,
                                                     @RequestParam String fin) {
        return ResponseEntity.ok(service.obtenerIngresosPorMedico(medicoId, inicio, fin));
    }

    @GetMapping("/ingresos/totales")
    public ResponseEntity<Double> ingresosTotales(@RequestParam String inicio,
                                                   @RequestParam String fin) {
        return ResponseEntity.ok(service.obtenerIngresosTotales(inicio, fin));
    }

    @GetMapping("/resumen-mensual")
    public ResponseEntity<?> resumenMensual(@RequestParam int anio, @RequestParam int mes) {
        return ResponseEntity.ok(service.obtenerResumenMensual(anio, mes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }
}