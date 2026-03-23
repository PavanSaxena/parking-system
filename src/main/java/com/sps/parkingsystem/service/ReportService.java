package com.sps.parkingsystem.service;

import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.repository.ParkingTicketRepository;
import com.sps.parkingsystem.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private final ParkingTicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    public ReportService(ParkingTicketRepository ticketRepository,  PaymentRepository paymentRepository) {
        this.ticketRepository = ticketRepository;
        this.paymentRepository = paymentRepository;
    }
    public List<ParkingTicket> getAllTickets() {
        return ticketRepository.findAll();
    }
    public double getTotalRevenue(){
        return paymentRepository.findAll()
                .stream()
                .mapToDouble(p -> p.getAmount())
                .sum();
    }
}
