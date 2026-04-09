package com.sps.parkingsystem.repository;

import com.sps.parkingsystem.model.VehicleOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleOwnerRepository extends JpaRepository<VehicleOwner, String> {
}
