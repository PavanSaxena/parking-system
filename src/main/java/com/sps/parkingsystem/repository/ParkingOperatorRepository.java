package com.sps.parkingsystem.repository;

import com.sps.parkingsystem.model.ParkingOperator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingOperatorRepository extends JpaRepository<ParkingOperator, String> {
	Optional<ParkingOperator> findByEmail(String email);
}
