package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.dto.TicketCreateRequest;
import com.sps.parkingsystem.dto.response.ParkingTicketResponse;
import com.sps.parkingsystem.mapper.ResponseMapper;
import com.sps.parkingsystem.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/entry")
    public ParkingTicketResponse createTicket(@Valid @RequestBody TicketCreateRequest request) {
        return ResponseMapper.toParkingTicketResponse(ticketService.createTicket(
                request.getTicketId(),
                request.getVehicleNumber(),
                request.getSlotId(),
                request.getOperatorId()
        ));
    }

    @PutMapping("/{ticketId}/exit")
    public ParkingTicketResponse closeTicket(@PathVariable("ticketId") String ticketId) {
        return ResponseMapper.toParkingTicketResponse(ticketService.closeTicket(ticketId));
    }
}
