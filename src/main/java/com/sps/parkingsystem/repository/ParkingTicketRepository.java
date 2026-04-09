package com.sps.parkingsystem.repository;

import com.sps.parkingsystem.model.ParkingTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, String> {
    Optional<ParkingTicket>
    findByVehicleVehicleNumberAndExitTimeIsNull(String vehicleNumber);
    List<ParkingTicket>
    findByExitTimeIsNull();
    long countByExitTimeIsNull();
}