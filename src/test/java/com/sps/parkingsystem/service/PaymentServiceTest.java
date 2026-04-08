package com.sps.parkingsystem.service;

import com.sps.parkingsystem.exception.InvalidParkingStateException;
import com.sps.parkingsystem.model.ParkingRate;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Payment;
import com.sps.parkingsystem.model.Vehicle;
import com.sps.parkingsystem.repository.ParkingRateRepository;
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
    private ParkingRateRepository rateRepository;

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
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType("CAR");

        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId("T1");
        ticket.setVehicle(vehicle);
        ticket.setEntryTime(LocalDateTime.now().minusMinutes(61));
        ticket.setExitTime(LocalDateTime.now());

        ParkingRate rate = new ParkingRate();
        rate.setVehicleType("CAR");
        rate.setHourlyRate(50.0);

        when(ticketRepository.findById("T1")).thenReturn(Optional.of(ticket));
        when(paymentRepository.findByTicketTicketId("T1")).thenReturn(Optional.empty());
        when(rateRepository.findByVehicleType("CAR")).thenReturn(Optional.of(rate));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.processPayment("T1");
        assertEquals(100.0, payment.getAmount());
    }
}

