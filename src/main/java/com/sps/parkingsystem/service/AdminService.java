package com.sps.parkingsystem.service;

import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.model.ParkingRate;
import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.repository.ParkingRateRepository;
import com.sps.parkingsystem.repository.ParkingSlotRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final ParkingSlotRepository slotRepository;
    private final ParkingRateRepository rateRepository;
    public AdminService(ParkingSlotRepository slotRepository, ParkingRateRepository rateRepository) {
        this.slotRepository = slotRepository;
        this.rateRepository = rateRepository;
    }

    public ParkingSlot addParkingSlot(String slotId, String slotType, SlotStatus status) {
        ParkingSlot slot = new ParkingSlot();
        slot.setSlotId(slotId);
        slot.setSlotType(slotType);
        slot.setStatus(status == null ? SlotStatus.AVAILABLE : status);
        return slotRepository.save(slot);
    }

    public void removeParkingSlot(String slot) {
        slotRepository.deleteById(slot);
    }

    public ParkingRate setParkingRate(ParkingRate rate){
        return rateRepository.save(rate);
    }
}