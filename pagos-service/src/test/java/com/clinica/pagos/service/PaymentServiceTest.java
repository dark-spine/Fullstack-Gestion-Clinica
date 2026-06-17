package com.clinica.pagos.service;

import com.clinica.pagos.dto.PaymentRequestDTO;
import com.clinica.pagos.dto.PaymentResponseDTO;
import com.clinica.pagos.event.PaymentEventPublisher;
import com.clinica.pagos.mapper.PaymentMapper;
import com.clinica.pagos.model.Payment;
import com.clinica.pagos.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Payment Service Tests")
class PaymentServiceTest {
    @Mock
    private PaymentRepository repository;

    @Mock
    private PaymentMapper mapper;

    @Mock
    private PaymentEventPublisher eventPublisher;

    @InjectMocks
    private PaymentService paymentService;

    private PaymentRequestDTO paymentRequestDTO;
    private Payment payment;
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

        payment = Payment.builder()
                .id(1L)
                .appointmentId(100L)
                .amount(new BigDecimal("150.50"))
                .status("COMPLETED")
                .type("CREDIT_CARD")
                .processedAt(processedAt)
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
    @DisplayName("Should process payment successfully")
    void testProcessPaymentSuccess() {
        // Arrange
        when(mapper.toEntity(paymentRequestDTO)).thenReturn(payment);
        when(repository.save(any(Payment.class))).thenReturn(payment);
        when(mapper.toResponse(payment)).thenReturn(paymentResponseDTO);

        // Act
        PaymentResponseDTO result = paymentService.processPayment(paymentRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        assertEquals(new BigDecimal("150.50"), result.getAmount());
        verify(repository, times(1)).save(any(Payment.class));
        verify(eventPublisher, times(1)).publishPaymentCompleted(payment);
    }

    @Test
    @DisplayName("Should refund payment successfully")
    void testRefundPaymentSuccess() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(payment));
        when(repository.save(any(Payment.class))).thenReturn(payment);
        when(mapper.toResponse(any(Payment.class))).thenReturn(paymentResponseDTO);

        // Act
        PaymentResponseDTO result = paymentService.refundPayment(1L);

        // Assert
        assertNotNull(result);
        verify(repository, times(1)).save(any(Payment.class));
        verify(eventPublisher, times(1)).publishPaymentRefunded(any(Payment.class));
    }

    @Test
    @DisplayName("Should list payments successfully")
    void testListPaymentsSuccess() {
        // Arrange
        Payment payment2 = Payment.builder()
                .id(2L)
                .appointmentId(101L)
                .amount(new BigDecimal("200.00"))
                .status("COMPLETED")
                .type("TRANSFER")
                .processedAt(processedAt)
                .build();

        PaymentResponseDTO responseDTO2 = PaymentResponseDTO.builder()
                .id(2L)
                .appointmentId(101L)
                .amount(new BigDecimal("200.00"))
                .status("COMPLETED")
                .type("TRANSFER")
                .processedAt(processedAt)
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(payment, payment2));
        when(mapper.toResponse(payment)).thenReturn(paymentResponseDTO);
        when(mapper.toResponse(payment2)).thenReturn(responseDTO2);

        // Act
        List<PaymentResponseDTO> result = paymentService.listPayments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should count payments successfully")
    void testCountPaymentsSuccess() {
        // Arrange
        when(repository.count()).thenReturn(10L);

        // Act
        Long count = paymentService.countPayments();

        // Assert
        assertEquals(10L, count);
        verify(repository, times(1)).count();
    }

    @Test
    @DisplayName("Should calculate total revenue correctly")
    void testTotalRevenueCalculation() {
        // Arrange
        Payment payment2 = Payment.builder()
                .id(2L)
                .appointmentId(101L)
                .amount(new BigDecimal("200.00"))
                .status("COMPLETED")
                .type("TRANSFER")
                .processedAt(processedAt)
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(payment, payment2));

        // Act
        Double totalRevenue = paymentService.totalRevenue();

        // Assert
        assertEquals(350.5, totalRevenue);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return 0.0 total revenue when no completed payments exist")
    void testTotalRevenueReturnsZeroWhenNoCompletedPayments() {
        // Arrange
        Payment refundedPayment = Payment.builder()
                .id(3L)
                .appointmentId(102L)
                .amount(new BigDecimal("100.00"))
                .status("REFUNDED")
                .type("CREDIT_CARD")
                .processedAt(processedAt)
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(refundedPayment));

        // Act
        Double totalRevenue = paymentService.totalRevenue();

        // Assert
        assertEquals(0.0, totalRevenue);
    }

    @Test
    @DisplayName("Should verify mapper is called during payment processing")
    void testMapperCalledDuringProcessing() {
        // Arrange
        when(mapper.toEntity(paymentRequestDTO)).thenReturn(payment);
        when(repository.save(any(Payment.class))).thenReturn(payment);
        when(mapper.toResponse(payment)).thenReturn(paymentResponseDTO);

        // Act
        paymentService.processPayment(paymentRequestDTO);

        // Assert
        verify(mapper, times(1)).toEntity(paymentRequestDTO);
        verify(mapper, times(1)).toResponse(payment);
    }
}
