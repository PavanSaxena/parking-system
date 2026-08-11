package com.sps.parkingsystem.service;

import com.sps.parkingsystem.model.ParkingRate;
import com.sps.parkingsystem.repository.ParkingRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingRateServiceTest {

    @Mock
    private ParkingRateRepository rateRepository;

    @InjectMocks
    private ParkingRateService parkingRateService;

    @Test
    void createRatePersistsAndReturnsRate() {
        ParkingRate rate = new ParkingRate();
        rate.setRateId("R1");
        rate.setVehicleType("CAR");
        rate.setHourlyRate(20.0);
        when(rateRepository.save(any(ParkingRate.class))).thenAnswer(inv -> inv.getArgument(0));

        ParkingRate result = parkingRateService.createRate("R1", "CAR", 20.0);

        assertEquals("R1", result.getRateId());
        verify(rateRepository).save(any(ParkingRate.class));
    }

    @Test
    void getAllRatesDelegatesToRepository() {
        ParkingRate rate = new ParkingRate();
        rate.setRateId("R2");
        when(rateRepository.findAll()).thenReturn(List.of(rate));

        List<ParkingRate> result = parkingRateService.getAllRates();

        assertEquals(1, result.size());
    }
}
