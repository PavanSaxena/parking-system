package com.sps.parkingsystem.repository;

import com.sps.parkingsystem.model.ParkingOperator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingOperatorRepository extends JpaRepository<ParkingOperator, String> {
}
