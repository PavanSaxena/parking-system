package com.sps.parkingsystem.repository;

import com.sps.parkingsystem.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, String> {
}
