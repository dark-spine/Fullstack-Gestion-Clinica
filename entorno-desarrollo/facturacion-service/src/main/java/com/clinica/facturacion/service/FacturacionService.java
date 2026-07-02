package com.clinica.facturacion.service;

import com.clinica.facturacion.dto.FacturaCreateDTO;
import com.clinica.facturacion.dto.FacturaDTO;
import com.clinica.facturacion.model.Factura;
import com.clinica.facturacion.repository.FacturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    private static final Logger log = LoggerFactory.getLogger(FacturaService.class);

    private final FacturaRepository repository;

    public FacturaService(FacturaRepository repository) {
        this.repository = repository;
    }

    private FacturaDTO toDTO(Factura factura) {
        FacturaDTO dto = new FacturaDTO();
        dto.setId(factura.getId());
        dto.setCitaId(factura.getCitaId());
        dto.setPacienteId(factura.getPacienteId());
        dto.setMedicoId(factura.getMedicoId());
        dto.setMontoTotal(factura.getMontoTotal());
        dto.setMetodoPago(factura.getMetodoPago());
        dto.setPagada(factura.getPagada());
        dto.setFechaPago(factura.getFechaPago());
        dto.setCreatedAt(factura.getCreatedAt());
        return dto;
    }

    private Factura toEntity(FacturaCreateDTO dto) {
        Factura factura = new Factura();
        factura.setCitaId(dto.getCitaId());
        factura.setPacienteId(dto.getPacienteId());
        factura.setMedicoId(dto.getMedicoId());
        factura.setMontoTotal(dto.getMontoTotal());
        factura.setMetodoPago(dto.getMetodoPago());
        factura.setPagada(false);
        factura.setCreatedAt(LocalDateTime.now());
        return factura;
    }


    public FacturaDTO crearFactura(FacturaCreateDTO request) {
        log.info("Creando factura para citaId={}, pacienteId={}", request.getCitaId(), request.getPacienteId());

        Factura factura = toEntity(request);
        Factura guardada = repository.save(factura);
        log.info("Factura creada con id={}", guardada.getId());

        return toDTO(guardada);
    }

    public List<FacturaDTO> listarTodos() {
        log.info("Listando todas las facturas");
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public FacturaDTO obtenerPorId(Long id) {
        log.info("Buscando factura con id={}", id);
        Factura factura = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        return toDTO(factura);
    }

    public List<FacturaDTO> listarPorPaciente(Long pacienteId) {
        log.info("Listando facturas del paciente id={}", pacienteId);
        return repository.findByPacienteId(pacienteId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<FacturaDTO> listarPorMedico(Long medicoId) {
        log.info("Listando facturas del médico id={}", medicoId);
        return repository.findByMedicoId(medicoId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public FacturaDTO marcarPagada(Long id, String fechaPagoStr) {
        log.info("Marcando factura id={} como pagada", id);

        Factura factura = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        factura.setPagada(true);
        if (fechaPagoStr != null && !fechaPagoStr.isEmpty()) {
            LocalDateTime fechaPago = LocalDateTime.parse(fechaPagoStr, DateTimeFormatter.ISO_DATE_TIME);
            factura.setFechaPago(fechaPago);
        } else {
            factura.setFechaPago(LocalDateTime.now());
        }

        Factura actualizada = repository.save(factura);
        log.info("Factura id={} marcada como pagada", id);

        return toDTO(actualizada);
    }

    public FacturaDTO cancelarFactura(Long id) {
        log.info("Cancelando factura id={}", id);
        Factura factura = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        repository.deleteById(id);
        log.info("Factura id={} cancelada (eliminada)", id);

        return toDTO(factura);
    }

    public Double obtenerIngresosPorMedico(Long medicoId, String inicioStr, String finStr) {
        log.info("Calculando ingresos del médico id={} entre {} y {}", medicoId, inicioStr, finStr);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime inicio = LocalDateTime.parse(inicioStr, formatter);
        LocalDateTime fin = LocalDateTime.parse(finStr, formatter);

        return repository.findByMedicoIdAndCreatedAtBetween(medicoId, inicio, fin).stream()
                .filter(Factura::getPagada)
                .mapToDouble(Factura::getMontoTotal)
                .sum();
    }

    public Double obtenerIngresosTotales(String inicioStr, String finStr) {
        log.info("Calculando ingresos totales entre {} y {}", inicioStr, finStr);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime inicio = LocalDateTime.parse(inicioStr, formatter);
        LocalDateTime fin = LocalDateTime.parse(finStr, formatter);

        return repository.findByCreatedAtBetween(inicio, fin).stream()
                .filter(Factura::getPagada)
                .mapToDouble(Factura::getMontoTotal)
                .sum();
    }

    public Object obtenerResumenMensual(int anio, int mes) {
        log.info("Generando resumen mensual para {}/{}", mes, anio);
        LocalDateTime inicio = LocalDateTime.of(anio, mes, 1, 0, 0);
        LocalDateTime fin = inicio.plusMonths(1).minusSeconds(1);

        Double total = repository.findByCreatedAtBetween(inicio, fin).stream()
                .filter(Factura::getPagada)
                .mapToDouble(Factura::MontoTotal)
                .sum();

        return java.util.Map.of(
                "anio", anio,
                "mes", mes,
                "totalIngresos", total,
                "cantidadFacturas", repository.findByCreatedAtBetween(inicio, fin).size()
        );
    }

    public void eliminarFactura(Long id) {
        log.info("Eliminando factura id={}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Factura no encontrada");
        }
        repository.deleteById(id);
        log.info("Factura id={} eliminada", id);
    }
}