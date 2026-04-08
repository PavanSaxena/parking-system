package com.sps.parkingsystem.service;

import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.exception.DuplicateVehicleEntryException;
import com.sps.parkingsystem.model.ParkingOperator;
import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Vehicle;
import com.sps.parkingsystem.repository.ParkingOperatorRepository;
import com.sps.parkingsystem.repository.ParkingRateRepository;
import com.sps.parkingsystem.repository.ParkingSlotRepository;
import com.sps.parkingsystem.repository.ParkingTicketRepository;
import com.sps.parkingsystem.repository.PaymentRepository;
import com.sps.parkingsystem.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private ParkingTicketRepository ticketRepository;
    @Mock
    private ParkingSlotRepository slotRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private ParkingOperatorRepository operatorRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private ParkingRateRepository rateRepository;

    @InjectMocks
    private ParkingService parkingService;

    @Test
    void enterVehicleOccupiesSlotAndCreatesTicket() {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA01AB1234");
        vehicle.setVehicleType("CAR");

        ParkingOperator operator = new ParkingOperator();
        operator.setUserId("OP-1");

        ParkingSlot slot = new ParkingSlot();
        slot.setSlotId("S1");
        slot.setSlotType("CAR");
        slot.setStatus(SlotStatus.AVAILABLE);

        when(vehicleRepository.findById("KA01AB1234")).thenReturn(Optional.of(vehicle));
        when(ticketRepository.findByVehicleVehicleNumberAndExitTimeIsNull("KA01AB1234")).thenReturn(Optional.empty());
        when(slotRepository.findFirstByStatusAndSlotType(SlotStatus.AVAILABLE, "CAR")).thenReturn(Optional.of(slot));
        when(operatorRepository.findById("OP-1")).thenReturn(Optional.of(operator));
        when(ticketRepository.save(any(ParkingTicket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParkingTicket created = parkingService.enterVehicle("KA01AB1234", "OP-1");

        assertEquals("KA01AB1234", created.getVehicle().getVehicleNumber());
        assertEquals("S1", created.getSlot().getSlotId());
        assertEquals(SlotStatus.OCCUPIED, slot.getStatus());

        ArgumentCaptor<ParkingSlot> slotCaptor = ArgumentCaptor.forClass(ParkingSlot.class);
        verify(slotRepository).save(slotCaptor.capture());
        assertEquals(SlotStatus.OCCUPIED, slotCaptor.getValue().getStatus());
    }

    @Test
    void enterVehicleThrowsWhenAlreadyParked() {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA01AB1234");

        when(vehicleRepository.findById("KA01AB1234")).thenReturn(Optional.of(vehicle));
        when(ticketRepository.findByVehicleVehicleNumberAndExitTimeIsNull("KA01AB1234"))
                .thenReturn(Optional.of(new ParkingTicket()));

        assertThrows(DuplicateVehicleEntryException.class,
                () -> parkingService.enterVehicle("KA01AB1234", "OP-1"));
    }
}

