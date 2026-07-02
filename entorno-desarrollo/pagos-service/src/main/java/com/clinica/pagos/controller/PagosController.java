package com.clinica.pagos.controller;

import com.clinica.pagos.dto.PagoCreateDTO;
import com.clinica.pagos.dto.PagoDTO;
import com.clinica.pagos.dto.PagoProcesarDTO;
import com.clinica.pagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService service;

    public PagoController(PagoService service) {
        this.service = service;
    }

    @PostMapping("/orden")
    public ResponseEntity<PagoDTO> crearOrdenPago(@Valid @RequestBody PagoCreateDTO request) {
        PagoDTO nuevaOrden = service.crearOrdenPago(request);
        return new ResponseEntity<>(nuevaOrden, HttpStatus.CREATED);
    }

    @PostMapping("/{ordenId}/procesar")
    public ResponseEntity<PagoDTO> procesarPago(
            @PathVariable Long ordenId,
            @Valid @RequestBody PagoProcesarDTO request) {
        PagoDTO pagoProcesado = service.procesarPago(ordenId, request);
        return ResponseEntity.ok(pagoProcesado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> obtenerPago(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPago(id));
    }

    @GetMapping("/orden/{numeroOrden}")
    public ResponseEntity<PagoDTO> obtenerPagoPorOrden(@PathVariable String numeroOrden) {
        return ResponseEntity.ok(service.obtenerPagoPorNumeroOrden(numeroOrden));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<PagoDTO>> listarPagosPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.listarPagosPorPaciente(pacienteId));
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<List<PagoDTO>> listarPagosPorCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(service.listarPagosPorCita(citaId));
    }

    @GetMapping("/cita/{citaId}/verificar")
    public ResponseEntity<Boolean> verificarPagoCita(@PathVariable Long citaId) {
        boolean pagado = service.verificarPagoCita(citaId);
        return ResponseEntity.ok(pagado);
    }

    @PostMapping("/{id}/reembolsar")
    public ResponseEntity<PagoDTO> reembolsarPago(
            @PathVariable Long id,
            @RequestParam(required = false) String motivo) {
        PagoDTO reembolsado = service.reembolsarPago(id, motivo);
        return ResponseEntity.ok(reembolsado);
    }

    @GetMapping("/ingresos/medico/{medicoId}")
    public ResponseEntity<Double> obtenerIngresosPorMedico(
            @PathVariable Long medicoId,
            @RequestParam String inicio,
            @RequestParam String fin) {
        return ResponseEntity.ok(service.obtenerIngresosPorMedico(medicoId, inicio, fin));
    }

    @GetMapping("/ingresos/totales")
    public ResponseEntity<Double> obtenerIngresosTotales(
            @RequestParam String inicio,
            @RequestParam String fin) {
        return ResponseEntity.ok(service.obtenerIngresosTotales(inicio, fin));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoDTO>> listarPagosPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.listarPagosPorEstado(estado));
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarOrden(@PathVariable Long id) {
        service.cancelarOrden(id);
        return ResponseEntity.noContent().build();
    }
}