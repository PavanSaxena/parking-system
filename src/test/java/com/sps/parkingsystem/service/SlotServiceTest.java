package com.sps.parkingsystem.service;

import com.sps.parkingsystem.dto.response.SlotStatisticsResponse;
import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.exception.ResourceNotFoundException;
import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.repository.ParkingSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SlotServiceTest {

    @Mock
    private ParkingSlotRepository slotRepository;

    @InjectMocks
    private SlotService slotService;

    @Test
    void getAvailableSlotsReturnsOnlyAvailable() {
        ParkingSlot s1 = new ParkingSlot("S1", "CAR", SlotStatus.AVAILABLE);
        when(slotRepository.findByStatus(SlotStatus.AVAILABLE)).thenReturn(List.of(s1));

        List<ParkingSlot> available = slotService.getAvailableSlots();
        assertEquals(1, available.size());
        assertEquals("S1", available.get(0).getSlotId());
    }

    @Test
    void occupySlotMarksSlotOccupiedAndSaves() {
        ParkingSlot slot = new ParkingSlot("S1", "CAR", SlotStatus.AVAILABLE);
        when(slotRepository.findById("S1")).thenReturn(Optional.of(slot));
        when(slotRepository.save(slot)).thenReturn(slot);

        ParkingSlot updated = slotService.occupySlot("S1");

        assertEquals(SlotStatus.OCCUPIED, updated.getStatus());
        verify(slotRepository).save(slot);
    }

    @Test
    void freeSlotMarksSlotAvailableAndSaves() {
        ParkingSlot slot = new ParkingSlot("S1", "CAR", SlotStatus.OCCUPIED);
        when(slotRepository.findById("S1")).thenReturn(Optional.of(slot));
        when(slotRepository.save(slot)).thenReturn(slot);

        ParkingSlot updated = slotService.freeSlot("S1");

        assertEquals(SlotStatus.AVAILABLE, updated.getStatus());
        verify(slotRepository).save(slot);
    }

    @Test
    void occupySlotThrowsForUnknownSlot() {
        when(slotRepository.findById("UNKNOWN")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> slotService.occupySlot("UNKNOWN"));
    }

    @Test
    void getStatisticsUsesRepositoryCounts() {
        when(slotRepository.count()).thenReturn(10L);
        when(slotRepository.countByStatus(SlotStatus.AVAILABLE)).thenReturn(6L);
        when(slotRepository.countByStatus(SlotStatus.OCCUPIED)).thenReturn(4L);

        SlotStatisticsResponse stats = slotService.getStatistics();
        assertEquals(10L, stats.getTotalSlots());
        assertEquals(6L, stats.getAvailableSlots());
        assertEquals(4L, stats.getOccupiedSlots());
    }
}


