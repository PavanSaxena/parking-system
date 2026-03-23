package com.sps.parkingsystem.service;

import com.sps.parkingsystem.enums.SlotStatus;
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
    public ParkingSlot occupySlot(String slotId) {
        ParkingSlot slot = slotRepository.findById(slotId).orElseThrow();
        slot.setStatus(SlotStatus.OCCUPIED);
        return slotRepository.save(slot);
    }
    public ParkingSlot freeSlot(String slotId) {
        ParkingSlot slot = slotRepository.findById(slotId).orElseThrow();
        slot.setStatus(SlotStatus.AVAILABLE);
        return slotRepository.save(slot);
    }
}
