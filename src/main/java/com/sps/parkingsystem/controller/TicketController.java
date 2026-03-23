package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.service.TicketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }
    @PostMapping("/entry")
    public ParkingTicket createTicket(@RequestBody ParkingTicket ticket) {
        return ticketService.createTicket(ticket);
    }
    @PutMapping("/{ticketId}/exit")
    public ParkingTicket closeTicket(@PathVariable("ticketId") String ticketId) {
        return ticketService.closeTicket(ticketId);
    }
}
