package com.sps.parkingsystem.service;

import com.sps.parkingsystem.exception.InvalidParkingStateException;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Payment;
import com.sps.parkingsystem.repository.ParkingTicketRepository;
import com.sps.parkingsystem.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private ParkingTicketRepository ticketRepository;
    @Mock
    private FeeService feeService;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void processPaymentThrowsWhenVehicleNotExited() {
        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId("T1");
        ticket.setEntryTime(LocalDateTime.now());

        when(ticketRepository.findById("T1")).thenReturn(Optional.of(ticket));

        assertThrows(InvalidParkingStateException.class, () -> paymentService.processPayment("T1"));
    }

    @Test
    void processPaymentReturnsExistingPaymentForIdempotency() {
        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId("T1");
        ticket.setEntryTime(LocalDateTime.now().minusHours(1));
        ticket.setExitTime(LocalDateTime.now());

        Payment existing = new Payment();
        existing.setPaymentId("P1");

        when(ticketRepository.findById("T1")).thenReturn(Optional.of(ticket));
        when(paymentRepository.findByTicketTicketId("T1")).thenReturn(Optional.of(existing));

        Payment payment = paymentService.processPayment("T1");
        assertEquals("P1", payment.getPaymentId());
    }

    @Test
    void processPaymentCalculatesRoundedHourlyAmount() {
        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId("T1");
        ticket.setEntryTime(LocalDateTime.now().minusMinutes(61));
        ticket.setExitTime(LocalDateTime.now());

        when(ticketRepository.findById("T1")).thenReturn(Optional.of(ticket));
        when(paymentRepository.findByTicketTicketId("T1")).thenReturn(Optional.empty());
        when(feeService.calculateFee(ticket)).thenReturn(100.0);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.processPayment("T1");
        assertEquals(100.0, payment.getAmount());
    }
}


