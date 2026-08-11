package com.sps.parkingsystem.service;

import com.sps.parkingsystem.exception.PaymentProcessingException;
import com.sps.parkingsystem.model.ParkingRate;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Vehicle;
import com.sps.parkingsystem.repository.ParkingRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeeServiceTest {

    @Mock
    private ParkingRateRepository rateRepository;

    @InjectMocks
    private FeeService feeService;

    @Test
    void calculateFeeThrowsWhenTicketDataIsIncomplete() {
        ParkingTicket ticket = new ParkingTicket();
        assertThrows(PaymentProcessingException.class, () -> feeService.calculateFee(ticket));
    }

    @Test
    void calculateFeeUsesRoundedHourlyBilling() {
        ParkingTicket ticket = new ParkingTicket();
        ticket.setEntryTime(LocalDateTime.now().minusMinutes(61));
        ticket.setExitTime(LocalDateTime.now());

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType("CAR");
        ticket.setVehicle(vehicle);

        ParkingRate rate = new ParkingRate();
        rate.setHourlyRate(20.0);

        when(rateRepository.findByVehicleType("CAR")).thenReturn(Optional.of(rate));

        double fee = feeService.calculateFee(ticket);

        assertEquals(40.0, fee);
    }
}
