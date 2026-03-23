package com.sps.parkingsystem.service;

import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.repository.ParkingTicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketService {
    private final ParkingTicketRepository ticketRepository;
    public TicketService(ParkingTicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    public ParkingTicket createTicket(ParkingTicket ticket) {
        ticket.setEntryTime(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }
    public ParkingTicket closeTicket(String ticketId) {
        ParkingTicket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setExitTime(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }
}
