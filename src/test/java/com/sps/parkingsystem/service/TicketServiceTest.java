package com.sps.parkingsystem.service;

import com.sps.parkingsystem.exception.ResourceNotFoundException;
import com.sps.parkingsystem.model.ParkingOperator;
import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Vehicle;
import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.repository.ParkingOperatorRepository;
import com.sps.parkingsystem.repository.ParkingSlotRepository;
import com.sps.parkingsystem.repository.ParkingTicketRepository;
import com.sps.parkingsystem.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private ParkingTicketRepository ticketRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ParkingSlotRepository slotRepository;

    @Mock
    private ParkingOperatorRepository operatorRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void createTicketMarksSlotOccupiedAndPersistsTicket() {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA01");
        ParkingSlot slot = new ParkingSlot("S1", "CAR", SlotStatus.AVAILABLE);
        ParkingOperator operator = new ParkingOperator();
        operator.setUserId("OP-1");

        when(vehicleRepository.findById("KA01")).thenReturn(Optional.of(vehicle));
        when(slotRepository.findById("S1")).thenReturn(Optional.of(slot));
        when(operatorRepository.findById("OP-1")).thenReturn(Optional.of(operator));
        when(ticketRepository.save(any(ParkingTicket.class))).thenAnswer(inv -> inv.getArgument(0));
        when(slotRepository.save(any(ParkingSlot.class))).thenAnswer(inv -> inv.getArgument(0));

        ParkingTicket ticket = ticketService.createTicket("T1", "KA01", "S1", "OP-1");

        assertEquals("T1", ticket.getTicketId());
        assertEquals(SlotStatus.OCCUPIED, slot.getStatus());
        verify(slotRepository).save(slot);
        verify(ticketRepository).save(any(ParkingTicket.class));
    }

    @Test
    void createTicketThrowsWhenVehicleMissing() {
        when(vehicleRepository.findById("KA01")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> ticketService.createTicket("T1", "KA01", "S1", "OP-1"));
    }

    @Test
    void closeTicketSetsExitTime() {
        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId("T1");
        when(ticketRepository.findById("T1")).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(ParkingTicket.class))).thenAnswer(inv -> inv.getArgument(0));

        ParkingTicket result = ticketService.closeTicket("T1");

        assertNotNull(result.getExitTime());
        assertEquals("T1", result.getTicketId());
    }

    @Test
    void closeTicketThrowsWhenTicketMissing() {
        when(ticketRepository.findById("T1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.closeTicket("T1"));
    }
}
