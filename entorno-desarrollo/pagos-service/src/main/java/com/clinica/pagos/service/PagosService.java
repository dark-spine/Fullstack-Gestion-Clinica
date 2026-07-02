package com.clinica.pagos.service;

import com.clinica.pagos.dto.PagoCreateDTO;
import com.clinica.pagos.dto.PagoDTO;
import com.clinica.pagos.dto.PagoProcesarDTO;
import com.clinica.pagos.model.Pago;
import com.clinica.pagos.repository.PagoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    private final PagoRepository repository;

    public PagoService(PagoRepository repository) {
        this.repository = repository;
    }

    private PagoDTO toDTO(Pago pago) {
        PagoDTO dto = new PagoDTO();
        dto.setId(pago.getId());
        dto.setNumeroOrden(pago.getNumeroOrden());
        dto.setCitaId(pago.getCitaId());
        dto.setPacienteId(pago.getPacienteId());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstado(pago.getEstado());
        dto.setFechaCreacion(pago.getFechaCreacion());
        dto.setFechaPago(pago.getFechaPago());
        return dto;
    }

    private Pago toEntity(PagoCreateDTO dto) {
        Pago pago = new Pago();
        pago.setCitaId(dto.getCitaId());
        pago.setPacienteId(dto.getPacienteId());
        pago.setMonto(dto.getMonto());
        return pago;
    }


    public PagoDTO crearOrdenPago(PagoCreateDTO request) {
        log.info("Creando orden de pago para citaId={}", request.getCitaId());

        Pago pago = toEntity(request);
        pago.setNumeroOrden(UUID.randomUUID().toString());
        pago.setEstado("PENDIENTE");
        pago.setFechaCreacion(LocalDateTime.now());

        Pago guardado = repository.save(pago);
        log.info("Orden de pago creada con id={}, numeroOrden={}", guardado.getId(), guardado.getNumeroOrden());

        return toDTO(guardado);
    }

    public PagoDTO procesarPago(Long ordenId, PagoProcesarDTO request) {
        log.info("Procesando pago para ordenId={}, metodoPago={}", ordenId, request.getMetodoPago());

        Pago pago = repository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException("Orden de pago no encontrada"));

        if (!"PENDIENTE".equals(pago.getEstado())) {
            throw new RuntimeException("La orden no está pendiente de pago");
        }

        pago.setMetodoPago(request.getMetodoPago());
        pago.setEstado("PAGADO");
        pago.setFechaPago(LocalDateTime.now());

        Pago guardado = repository.save(pago);
        log.info("Pago procesado exitosamente para ordenId={}", ordenId);

        return toDTO(guardado);
    }

    public PagoDTO obtenerPago(Long id) {
        log.info("Buscando pago con id={}", id);
        Pago pago = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return toDTO(pago);
    }

    public PagoDTO obtenerPagoPorNumeroOrden(String numeroOrden) {
        log.info("Buscando pago por numeroOrden={}", numeroOrden);
        Pago pago = repository.findByNumeroOrden(numeroOrden)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return toDTO(pago);
    }

    public List<PagoDTO> listarPagosPorPaciente(Long pacienteId) {
        log.info("Listando pagos del paciente id={}", pacienteId);
        return repository.findByPacienteId(pacienteId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<PagoDTO> listarPagosPorCita(Long citaId) {
        log.info("Listando pagos de la cita id={}", citaId);
        return repository.findByCitaId(citaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public boolean verificarPagoCita(Long citaId) {
        log.info("Verificando si la cita id={} está pagada", citaId);
        return repository.findByCitaId(citaId).stream()
                .anyMatch(p -> "PAGADO".equals(p.getEstado()));
    }

    public PagoDTO reembolsarPago(Long id, String motivo) {
        log.info("Reembolsando pago id={}, motivo={}", id, motivo);

        Pago pago = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if (!"PAGADO".equals(pago.getEstado())) {
            throw new RuntimeException("Solo se pueden reembolsar pagos que ya fueron procesados");
        }

        pago.setEstado("REEMBOLSADO");
        Pago reembolsado = repository.save(pago);
        log.info("Pago id={} reembolsado", id);

        return toDTO(reembolsado);
    }

    public Double obtenerIngresosPorMedico(Long medicoId, String inicioStr, String finStr) {
        log.info("Calculando ingresos del médico id={} entre {} y {}", medicoId, inicioStr, finStr);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime inicio = LocalDateTime.parse(inicioStr, formatter);
        LocalDateTime fin = LocalDateTime.parse(finStr, formatter);

        return repository.findByFechaPagoBetween(inicio, fin).stream()
                .filter(p -> "PAGADO".equals(p.getEstado()))
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    public Double obtenerIngresosTotales(String inicioStr, String finStr) {
        log.info("Calculando ingresos totales entre {} y {}", inicioStr, finStr);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime inicio = LocalDateTime.parse(inicioStr, formatter);
        LocalDateTime fin = LocalDateTime.parse(finStr, formatter);

        return repository.findByFechaPagoBetween(inicio, fin).stream()
                .filter(p -> "PAGADO".equals(p.getEstado()))
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    public List<PagoDTO> listarPagosPorEstado(String estado) {
        log.info("Listando pagos con estado={}", estado);
        return repository.findByEstado(estado).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void cancelarOrden(Long id) {
        log.info("Cancelando orden de pago id={}", id);
        Pago pago = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        if (!"PENDIENTE".equals(pago.getEstado())) {
            throw new RuntimeException("Solo se pueden cancelar órdenes en estado PENDIENTE");
        }

        repository.deleteById(id);
        log.info("Orden de pago id={} cancelada", id);
    }
}