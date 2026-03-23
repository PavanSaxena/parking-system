package com.sps.parkingsystem.service;

import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.repository.ParkingSlotRepository;
import com.sps.parkingsystem.repository.ParkingTicketRepository;
import com.sps.parkingsystem.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParkingService {
    private final ParkingTicketRepository ticketRepository;
    private final ParkingSlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;
    public ParkingService(ParkingTicketRepository ticketRepository,
                          ParkingSlotRepository slotRepository,
                          VehicleRepository vehicleRepository) {
        this.ticketRepository = ticketRepository;
        this.slotRepository = slotRepository;
        this.vehicleRepository = vehicleRepository;
    }
    public ParkingTicket enterVehicle(ParkingTicket ticket, String slotId, String operatorId) {
        if(ticket.getVehicle() != null){
            vehicleRepository.save(ticket.getVehicle());
        }
        ticket.setEntryTime(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }
    public ParkingTicket exitVehicle(String ticketId) {
        ParkingTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setExitTime(LocalDateTime.now());
        ParkingSlot slot = ticket.getSlot();
        slot.setStatus(SlotStatus.AVAILABLE);
        slotRepository.save(slot);
        return ticketRepository.save(ticket);
    }
}