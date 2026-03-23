package com.sps.parkingsystem.service;

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
    public ParkingSlot addParkingSlot(ParkingSlot slot) {
        return slotRepository.save(slot);
    }
    public void removeParkingSlot(String slot) {
        slotRepository.deleteById(slot);
    }
    public ParkingRate setParkingRate(ParkingRate rate){
        return rateRepository.save(rate);
    }
}