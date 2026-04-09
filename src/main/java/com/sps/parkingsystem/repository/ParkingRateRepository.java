package com.sps.parkingsystem.repository;

import com.sps.parkingsystem.model.ParkingRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingRateRepository extends JpaRepository<ParkingRate, String> {
    Optional<ParkingRate> findByVehicleType(String vehicleType);
}