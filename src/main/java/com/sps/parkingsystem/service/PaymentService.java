package com.sps.parkingsystem.service;

import com.sps.parkingsystem.enums.PaymentStatus;
import com.sps.parkingsystem.exception.InvalidParkingStateException;
import com.sps.parkingsystem.exception.ResourceNotFoundException;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Payment;
import com.sps.parkingsystem.repository.ParkingTicketRepository;
import com.sps.parkingsystem.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ParkingTicketRepository ticketRepository;
    private final FeeService feeService;

    public PaymentService(PaymentRepository paymentRepository,
                          ParkingTicketRepository ticketRepository,
                          FeeService feeService) {
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.feeService = feeService;
    }

    @Transactional
    public Payment processPayment(String ticketId){
        ParkingTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketId));

        if (ticket.getExitTime() == null) {
            throw new InvalidParkingStateException("Cannot process payment before vehicle exit");
        }

        Payment existingPayment = paymentRepository.findByTicketTicketId(ticketId).orElse(null);
        if (existingPayment != null) {
            return existingPayment;
        }

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setAmount(feeService.calculateFee(ticket));
        payment.setTicket(ticket);
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentTime(LocalDateTime.now());
        return paymentRepository.save(payment);
    }
}
