package com.sps.parkingsystem.service;

import com.sps.parkingsystem.model.ParkingRate;
import com.sps.parkingsystem.repository.ParkingRateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingRateService {
    private final ParkingRateRepository rateRepository;
    public ParkingRateService(ParkingRateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }
    public ParkingRate createRate(String rateId, String vehicleType, double hourlyRate){
        ParkingRate rate = new ParkingRate();
        rate.setRateId(rateId);
        rate.setVehicleType(vehicleType);
        rate.setHourlyRate(hourlyRate);
        return rateRepository.save(rate);
    }
    public List<ParkingRate> getAllRates(){
        return rateRepository.findAll();
    }
}
