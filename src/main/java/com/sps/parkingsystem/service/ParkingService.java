package com.sps.parkingsystem.service;

import com.sps.parkingsystem.model.*;
import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.exception.DuplicateVehicleEntryException;
import com.sps.parkingsystem.exception.InvalidParkingStateException;
import com.sps.parkingsystem.exception.ResourceNotFoundException;
import com.sps.parkingsystem.exception.SlotUnavailableException;
import com.sps.parkingsystem.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ParkingService {
    private final ParkingTicketRepository ticketRepository;
    private final ParkingSlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingOperatorRepository operatorRepository;

    public ParkingService(ParkingTicketRepository ticketRepository,
                          ParkingSlotRepository slotRepository,
                          VehicleRepository vehicleRepository,
                          ParkingOperatorRepository operatorRepository) {
        this.ticketRepository = ticketRepository;
        this.slotRepository = slotRepository;
        this.vehicleRepository = vehicleRepository;
        this.operatorRepository = operatorRepository;
    }

    @Transactional
    public ParkingTicket enterVehicle(String vehicleNumber, String operatorId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not registered: " + vehicleNumber));
        boolean alreadyParked = ticketRepository.findByVehicleVehicleNumberAndExitTimeIsNull(vehicleNumber).isPresent();
        if (alreadyParked) {
            throw new DuplicateVehicleEntryException("Vehicle already parked inside parking");
        }
        ParkingSlot slot = slotRepository.findFirstByStatusAndSlotType(SlotStatus.AVAILABLE, vehicle.getVehicleType())
                .or(() -> slotRepository.findFirstByStatus(SlotStatus.AVAILABLE))
                .orElseThrow(() -> new SlotUnavailableException("No available slots"));
        ParkingOperator operator = operatorRepository.findById(operatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Operator not found: " + operatorId));

        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId(UUID.randomUUID().toString());
        ticket.setVehicle(vehicle);
        ticket.setOperator(operator);
        ticket.setSlot(slot);
        ticket.setEntryTime(LocalDateTime.now());

        slot.setStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public ParkingTicket exitVehicle(String ticketId) {
        ParkingTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketId));
        if (ticket.getExitTime() != null) {
            throw new InvalidParkingStateException("Vehicle already exited for ticket: " + ticketId);
        }

        ticket.setExitTime(LocalDateTime.now());
        ParkingSlot slot = ticket.getSlot();
        if (slot == null) {
            throw new InvalidParkingStateException("Ticket has no assigned slot: " + ticketId);
        }
        slot.setStatus(SlotStatus.AVAILABLE);
        slotRepository.save(slot);
        return ticketRepository.save(ticket);

    }
}