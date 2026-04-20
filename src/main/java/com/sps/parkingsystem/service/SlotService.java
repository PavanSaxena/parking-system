package com.sps.parkingsystem.service;

public class Slpackage com.sps.parkingsystem.service;

import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.dto.response.SlotStatisticsResponse;
import com.sps.parkingsystem.exception.ResourceNotFoundException;
import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.repository.ParkingSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotService {
    private final ParkingSlotRepository slotRepository;
    public SlotService(ParkingSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }
    public List<ParkingSlot> getAllSlots(){
        return slotRepository.findAll();
    }

    public List<ParkingSlot> getAvailableSlots() {
        return slotRepository.findByStatus(SlotStatus.AVAILABLE);
    }

    public List<ParkingSlot> getOccupiedSlots() {
        return slotRepository.findByStatus(SlotStatus.OCCUPIED);
    }

    public List<ParkingSlot> getSlotsByType(String slotType) {
        return slotRepository.findBySlotType(slotType);
    }

    public ParkingSlot occupySlot(String slotId) {
        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + slotId));
        slot.setStatus(SlotStatus.OCCUPIED);
        return slotRepository.save(slot);
    }

    public ParkingSlot freeSlot(String slotId) {
        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + slotId));
        slot.setStatus(SlotStatus.AVAILABLE);
        return slotRepository.save(slot);
    }

    public SlotStatisticsResponse getStatistics() {
        long total = slotRepository.count();
        long available = slotRepository.countByStatus(SlotStatus.AVAILABLE);
        long occupied = slotRepository.countByStatus(SlotStatus.OCCUPIED);
        return new SlotStatisticsResponse(total, available, occupied);
    }
}otService {
    
}
