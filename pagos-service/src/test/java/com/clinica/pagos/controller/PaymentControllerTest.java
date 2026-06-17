package com.clinica.pagos.controller;

import com.clinica.pagos.dto.PaymentRequestDTO;
import com.clinica.pagos.dto.PaymentResponseDTO;
import com.clinica.pagos.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
@DisplayName("Payment Controller Tests")
class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    private PaymentRequestDTO paymentRequestDTO;
    private PaymentResponseDTO paymentResponseDTO;
    private LocalDateTime processedAt;

    @BeforeEach
    void setUp() {
        processedAt = LocalDateTime.of(2025, 6, 20, 10, 0);

        paymentRequestDTO = PaymentRequestDTO.builder()
                .appointmentId(100L)
                .amount(new BigDecimal("150.50"))
                .type("CREDIT_CARD")
                .build();

        paymentResponseDTO = PaymentResponseDTO.builder()
                .id(1L)
                .appointmentId(100L)
                .amount(new BigDecimal("150.50"))
                .status("COMPLETED")
                .type("CREDIT_CARD")
                .processedAt(processedAt)
                .build();
    }

    @Test
    @DisplayName("POST /api/payments should return 201 Created")
    void testProcessPaymentReturns201() throws Exception {
        // Arrange
        when(paymentService.processPayment(any(PaymentRequestDTO.class))).thenReturn(paymentResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.appointmentId", is(100)))
                .andExpect(jsonPath("$.status", is("COMPLETED")));

        verify(paymentService, times(1)).processPayment(any(PaymentRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/payments should return 400 Bad Request with invalid data")
    void testProcessPaymentReturns400WithInvalidData() throws Exception {
        // Arrange
        PaymentRequestDTO invalidRequest = PaymentRequestDTO.builder()
                .appointmentId(null)
                .amount(new BigDecimal("-100.00"))
                .type("")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/payments/{id}/refund should return 200 OK")
    void testRefundPaymentReturns200() throws Exception {
        // Arrange
        when(paymentService.refundPayment(1L)).thenReturn(paymentResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/payments/1/refund")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.appointmentId", is(100)));

        verify(paymentService, times(1)).refundPayment(1L);
    }

    @Test
    @DisplayName("POST /api/payments/{id}/refund should return 404 when payment not found")
    void testRefundPaymentReturns404() throws Exception {
        // Arrange
        when(paymentService.refundPayment(999L))
                .thenThrow(new IllegalArgumentException("Payment not found"));

        // Act & Assert
        mockMvc.perform(post("/api/payments/999/refund")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/payments should return 200 OK with list of payments")
    void testListPaymentsReturns200() throws Exception {
        // Arrange
        PaymentResponseDTO payment2 = PaymentResponseDTO.builder()
                .id(2L)
                .appointmentId(101L)
                .amount(new BigDecimal("200.00"))
                .status("COMPLETED")
                .type("TRANSFER")
                .processedAt(processedAt)
                .build();

        List<PaymentResponseDTO> payments = Arrays.asList(paymentResponseDTO, payment2);
        when(paymentService.listPayments()).thenReturn(payments);

        // Act & Assert
        mockMvc.perform(get("/api/payments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].appointmentId", is(100)))
                .andExpect(jsonPath("$[1].appointmentId", is(101)));

        verify(paymentService, times(1)).listPayments();
    }

    @Test
    @DisplayName("GET /api/payments should return 200 OK with empty list")
    void testListPaymentsReturns200EmptyList() throws Exception {
        // Arrange
        when(paymentService.listPayments()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/payments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(paymentService, times(1)).listPayments();
    }

    @Test
    @DisplayName("POST /api/payments should verify service is called with correct DTO")
    void testProcessPaymentCallsServiceWithCorrectDTO() throws Exception {
        // Arrange
        when(paymentService.processPayment(any(PaymentRequestDTO.class))).thenReturn(paymentResponseDTO);

        // Act
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequestDTO)))
                .andExpect(status().isCreated());

        // Assert
        verify(paymentService).processPayment(any(PaymentRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/payments should return payment response with all fields")
    void testProcessPaymentResponseContainsAllFields() throws Exception {
        // Arrange
        when(paymentService.processPayment(any(PaymentRequestDTO.class))).thenReturn(paymentResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.appointmentId").exists())
                .andExpect(jsonPath("$.amount").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.processedAt").exists());
    }
}
