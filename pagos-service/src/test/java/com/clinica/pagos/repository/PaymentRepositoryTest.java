package com.clinica.pagos.repository;

import com.clinica.pagos.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Payment Repository Tests")
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository repository;

    private Payment payment;
    private LocalDateTime processedAt;

    @BeforeEach
    void setUp() {
        processedAt = LocalDateTime.of(2025, 6, 20, 10, 0);

        payment = Payment.builder()
                .appointmentId(100L)
                .amount(new BigDecimal("150.50"))
                .status("COMPLETED")
                .type("CREDIT_CARD")
                .processedAt(processedAt)
                .build();
    }

    @Test
    @DisplayName("Should save payment successfully")
    void testSavePaymentSuccess() {
        // Act
        Payment savedPayment = repository.save(payment);

        // Assert
        assertNotNull(savedPayment.getId());
        assertEquals(100L, savedPayment.getAppointmentId());
        assertEquals(new BigDecimal("150.50"), savedPayment.getAmount());
        assertEquals("COMPLETED", savedPayment.getStatus());
    }

    @Test
    @DisplayName("Should find payment by id")
    void testFindById() {
        // Arrange
        Payment savedPayment = repository.save(payment);

        // Act
        Optional<Payment> foundPayment = repository.findById(savedPayment.getId());

        // Assert
        assertTrue(foundPayment.isPresent());
        assertEquals(savedPayment.getId(), foundPayment.get().getId());
        assertEquals(100L, foundPayment.get().getAppointmentId());
    }

    @Test
    @DisplayName("Should return empty Optional when payment not found")
    void testFindByIdNotFound() {
        // Act
        Optional<Payment> foundPayment = repository.findById(999L);

        // Assert
        assertTrue(foundPayment.isEmpty());
    }

    @Test
    @DisplayName("Should find payment by appointmentId")
    void testFindByAppointmentId() {
        // Arrange
        repository.save(payment);

        // Act
        List<Payment> foundPayments = repository.findByAppointmentId(100L);

        // Assert
        assertEquals(1, foundPayments.size());
        assertEquals(100L, foundPayments.get(0).getAppointmentId());
    }

    @Test
    @DisplayName("Should return empty list when no payments found for appointmentId")
    void testFindByAppointmentIdReturnsEmptyList() {
        // Act
        List<Payment> foundPayments = repository.findByAppointmentId(999L);

        // Assert
        assertTrue(foundPayments.isEmpty());
    }

    @Test
    @DisplayName("Should find all payments")
    void testFindAll() {
        // Arrange
        Payment payment2 = Payment.builder()
                .appointmentId(101L)
                .amount(new BigDecimal("200.00"))
                .status("COMPLETED")
                .type("TRANSFER")
                .processedAt(processedAt)
                .build();

        repository.save(payment);
        repository.save(payment2);

        // Act
        List<Payment> allPayments = repository.findAll();

        // Assert
        assertEquals(2, allPayments.size());
    }

    @Test
    @DisplayName("Should update payment status successfully")
    void testUpdatePaymentStatus() {
        // Arrange
        Payment savedPayment = repository.save(payment);
        savedPayment.setStatus("REFUNDED");

        // Act
        Payment updatedPayment = repository.save(savedPayment);

        // Assert
        assertEquals("REFUNDED", updatedPayment.getStatus());
    }

    @Test
    @DisplayName("Should update payment amount successfully")
    void testUpdatePaymentAmount() {
        // Arrange
        Payment savedPayment = repository.save(payment);
        savedPayment.setAmount(new BigDecimal("300.00"));

        // Act
        Payment updatedPayment = repository.save(savedPayment);

        // Assert
        assertEquals(new BigDecimal("300.00"), updatedPayment.getAmount());
    }

    @Test
    @DisplayName("Should delete payment successfully")
    void testDeletePaymentSuccess() {
        // Arrange
        Payment savedPayment = repository.save(payment);
        Long paymentId = savedPayment.getId();

        // Act
        repository.deleteById(paymentId);

        // Assert
        Optional<Payment> deletedPayment = repository.findById(paymentId);
        assertTrue(deletedPayment.isEmpty());
    }

    @Test
    @DisplayName("Should count all payments")
    void testCountAllPayments() {
        // Arrange
        Payment payment2 = Payment.builder()
                .appointmentId(101L)
                .amount(new BigDecimal("200.00"))
                .status("COMPLETED")
                .type("TRANSFER")
                .processedAt(processedAt)
                .build();

        repository.save(payment);
        repository.save(payment2);

        // Act
        long count = repository.count();

        // Assert
        assertEquals(2, count);
    }
}
