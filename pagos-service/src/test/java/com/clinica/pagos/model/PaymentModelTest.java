package com.clinica.pagos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Payment Model Tests")
class PaymentModelTest {
    private Payment payment;
    private LocalDateTime processedAt;

    @BeforeEach
    void setUp() {
        processedAt = LocalDateTime.of(2025, 6, 20, 10, 0);

        payment = Payment.builder()
                .id(1L)
                .appointmentId(100L)
                .amount(new BigDecimal("150.50"))
                .status("COMPLETED")
                .type("CREDIT_CARD")
                .processedAt(processedAt)
                .build();
    }

    @Test
    @DisplayName("Should create Payment with all fields")
    void testCreatePaymentWithAllFields() {
        // Act
        Payment testPayment = new Payment(2L, 101L, new BigDecimal("200.00"), "COMPLETED", "TRANSFER", processedAt);

        // Assert
        assertNotNull(testPayment);
        assertEquals(2L, testPayment.getId());
        assertEquals(101L, testPayment.getAppointmentId());
        assertEquals(new BigDecimal("200.00"), testPayment.getAmount());
        assertEquals("COMPLETED", testPayment.getStatus());
        assertEquals("TRANSFER", testPayment.getType());
        assertEquals(processedAt, testPayment.getProcessedAt());
    }

    @Test
    @DisplayName("Should set and get appointmentId")
    void testSetAndGetAppointmentId() {
        // Arrange
        Long newAppointmentId = 200L;

        // Act
        payment.setAppointmentId(newAppointmentId);

        // Assert
        assertEquals(newAppointmentId, payment.getAppointmentId());
    }

    @Test
    @DisplayName("Should set and get amount")
    void testSetAndGetAmount() {
        // Arrange
        BigDecimal newAmount = new BigDecimal("300.75");

        // Act
        payment.setAmount(newAmount);

        // Assert
        assertEquals(newAmount, payment.getAmount());
    }

    @Test
    @DisplayName("Should set and get status")
    void testSetAndGetStatus() {
        // Arrange
        String newStatus = "REFUNDED";

        // Act
        payment.setStatus(newStatus);

        // Assert
        assertEquals(newStatus, payment.getStatus());
    }

    @Test
    @DisplayName("Should set and get type")
    void testSetAndGetType() {
        // Arrange
        String newType = "DEBIT_CARD";

        // Act
        payment.setType(newType);

        // Assert
        assertEquals(newType, payment.getType());
    }

    @Test
    @DisplayName("Should set and get processedAt")
    void testSetAndGetProcessedAt() {
        // Arrange
        LocalDateTime newProcessedAt = LocalDateTime.of(2025, 6, 21, 14, 0);

        // Act
        payment.setProcessedAt(newProcessedAt);

        // Assert
        assertEquals(newProcessedAt, payment.getProcessedAt());
    }

    @Test
    @DisplayName("Should test equality between two identical payments")
    void testEqualityBetweenIdenticalPayments() {
        // Arrange
        Payment payment1 = Payment.builder()
                .id(1L)
                .appointmentId(100L)
                .amount(new BigDecimal("150.50"))
                .status("COMPLETED")
                .type("CREDIT_CARD")
                .processedAt(processedAt)
                .build();

        Payment payment2 = Payment.builder()
                .id(1L)
                .appointmentId(100L)
                .amount(new BigDecimal("150.50"))
                .status("COMPLETED")
                .type("CREDIT_CARD")
                .processedAt(processedAt)
                .build();

        // Assert
        assertEquals(payment1, payment2);
    }

    @Test
    @DisplayName("Should test inequality between different payments")
    void testInequalityBetweenDifferentPayments() {
        // Arrange
        Payment differentPayment = Payment.builder()
                .id(2L)
                .appointmentId(101L)
                .amount(new BigDecimal("250.00"))
                .status("PENDING")
                .type("TRANSFER")
                .processedAt(LocalDateTime.of(2025, 6, 21, 10, 0))
                .build();

        // Assert
        assertNotEquals(payment, differentPayment);
    }

    @Test
    @DisplayName("Should verify all fields are non-null when using Builder")
    void testAllFieldsNonNullWithBuilder() {
        // Arrange
        Payment builtPayment = Payment.builder()
                .id(3L)
                .appointmentId(102L)
                .amount(new BigDecimal("350.00"))
                .status("COMPLETED")
                .type("CREDIT_CARD")
                .processedAt(processedAt)
                .build();

        // Assert
        assertAll(
                () -> assertNotNull(builtPayment.getId()),
                () -> assertNotNull(builtPayment.getAppointmentId()),
                () -> assertNotNull(builtPayment.getAmount()),
                () -> assertNotNull(builtPayment.getStatus()),
                () -> assertNotNull(builtPayment.getType()),
                () -> assertNotNull(builtPayment.getProcessedAt())
        );
    }
}
