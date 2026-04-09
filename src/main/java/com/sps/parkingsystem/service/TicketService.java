package com.sps.parkingsystem.service;

import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.exception.ResourceNotFoundException;
import com.sps.parkingsystem.model.ParkingOperator;
import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Vehicle;
import com.sps.parkingsystem.repository.ParkingOperatorRepository;
import com.sps.parkingsystem.repository.ParkingSlotRepository;
import com.sps.parkingsystem.repository.ParkingTicketRepository;
import com.sps.parkingsystem.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketService {
    private final ParkingTicketRepository ticketRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSlotRepository slotRepository;
    private final ParkingOperatorRepository operatorRepository;

    public TicketService(ParkingTicketRepository ticketRepository,
                         VehicleRepository vehicleRepository,
                         ParkingSlotRepository slotRepository,
                         ParkingOperatorRepository operatorRepository) {
        this.ticketRepository = ticketRepository;
        this.vehicleRepository = vehicleRepository;
        this.slotRepository = slotRepository;
        this.operatorRepository = operatorRepository;
    }

    public ParkingTicket createTicket(String ticketId, String vehicleNumber, String slotId, String operatorId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + vehicleNumber));
        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + slotId));
        ParkingOperator operator = operatorRepository.findById(operatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Operator not found: " + operatorId));

        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId(ticketId);
        ticket.setVehicle(vehicle);
        ticket.setSlot(slot);
        ticket.setOperator(operator);
        ticket.setEntryTime(LocalDateTime.now());

        slot.setStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);
        return ticketRepository.save(ticket);
    }

    public ParkingTicket closeTicket(String ticketId) {
        ParkingTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketId));
        ticket.setExitTime(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }
}
