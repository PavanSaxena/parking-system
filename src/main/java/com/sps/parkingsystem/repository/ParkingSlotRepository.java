package com.sps.parkingsystem.repository;

import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.model.ParkingRate;
import com.sps.parkingsystem.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, String> {
    List<ParkingSlot> findByStatus(SlotStatus status);
}
