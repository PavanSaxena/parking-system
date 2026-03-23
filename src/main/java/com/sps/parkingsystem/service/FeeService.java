package com.sps.parkingsystem.service;

import com.sps.parkingsystem.model.ParkingRate;
import com.sps.parkingsystem.repository.ParkingRateRepository;
import org.springframework.stereotype.Service;

@Service
public class FeeService {
    private final ParkingRateRepository rateRepository;
    public FeeService(ParkingRateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }
    public double calculateFee(String vehicleType, long hours) {
        ParkingRate rate = rateRepository.findByVehicleType(vehicleType).orElseThrow();
        return rate.getHourlyRate() * hours;
    }
}
