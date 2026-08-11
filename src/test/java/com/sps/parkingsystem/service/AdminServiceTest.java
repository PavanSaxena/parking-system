package com.sps.parkingsystem.service;

import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.model.ParkingRate;
import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.repository.ParkingRateRepository;
import com.sps.parkingsystem.repository.ParkingSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private ParkingSlotRepository slotRepository;

    @Mock
    private ParkingRateRepository rateRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void addParkingSlotDefaultsToAvailableWhenStatusNull() {
        ParkingSlot slot = new ParkingSlot();
        slot.setSlotId("S10");
        when(slotRepository.save(any(ParkingSlot.class))).thenAnswer(inv -> inv.getArgument(0));

        ParkingSlot result = adminService.addParkingSlot("S10", "CAR", null);

        assertEquals(SlotStatus.AVAILABLE, result.getStatus());
        verify(slotRepository).save(any(ParkingSlot.class));
    }

    @Test
    void removeParkingSlotDelegatesToRepository() {
        adminService.removeParkingSlot("S10");
        verify(slotRepository).deleteById("S10");
    }

    @Test
    void setParkingRatePersistsRate() {
        ParkingRate rate = new ParkingRate();
        when(rateRepository.save(any(ParkingRate.class))).thenAnswer(inv -> inv.getArgument(0));

        ParkingRate result = adminService.setParkingRate(rate);

        assertEquals(rate, result);
        verify(rateRepository).save(rate);
    }
}
