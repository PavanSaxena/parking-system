package com.sps.parkingsystem.repository;

import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, String> {
    Optional<ParkingSlot>
    findFirstByStatus(SlotStatus status);
    Optional<ParkingSlot>
    findFirstByStatusAndSlotType(SlotStatus status, String slotType);
    List<ParkingSlot>
    findByStatus(SlotStatus status);
    List<ParkingSlot>
    findBySlotType(String slotType);
    long countByStatus(SlotStatus status);
}