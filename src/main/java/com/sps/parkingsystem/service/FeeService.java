package com.sps.parkingsystem.service;

import com.sps.parkingsystem.exception.PaymentProcessingException;
import com.sps.parkingsystem.model.ParkingRate;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.repository.ParkingRateRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class FeeService {
    private final ParkingRateRepository rateRepository;

    public FeeService(ParkingRateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public double calculateFee(ParkingTicket ticket) {
        if (ticket.getEntryTime() == null || ticket.getExitTime() == null || ticket.getVehicle() == null) {
            throw new PaymentProcessingException("Unable to calculate fee due to missing ticket data");
        }

        String vehicleType = ticket.getVehicle().getVehicleType();
        ParkingRate rate = rateRepository.findByVehicleType(vehicleType)
                .orElseThrow(() -> new PaymentProcessingException("Rate not found for vehicle type: " + vehicleType));

        long minutes = Math.max(1, Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toMinutes());
        long billedHours = (minutes + 59) / 60;
        return billedHours * rate.getHourlyRate();
    }
}
