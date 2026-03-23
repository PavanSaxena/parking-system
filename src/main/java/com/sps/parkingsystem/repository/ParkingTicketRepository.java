package com.sps.parkingsystem.repository;

import com.sps.parkingsystem.model.ParkingTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, String> {
    List<ParkingTicket> findByVehicleVehicleNumber(String vehicleNumber);
}
