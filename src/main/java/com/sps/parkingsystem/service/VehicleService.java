package com.sps.parkingsystem.service;

import com.sps.parkingsystem.exception.ResourceNotFoundException;
import com.sps.parkingsystem.model.Vehicle;
import com.sps.parkingsystem.model.VehicleOwner;
import com.sps.parkingsystem.repository.VehicleOwnerRepository;
import com.sps.parkingsystem.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleOwnerRepository ownerRepository;
    public VehicleService(VehicleRepository vehicleRepository, VehicleOwnerRepository ownerRepository){
        this.vehicleRepository = vehicleRepository;
        this.ownerRepository = ownerRepository;
    }
    public Vehicle createVehicle(String vehicleNumber, String vehicleType, String ownerId) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(vehicleNumber);
        vehicle.setVehicleType(vehicleType);
        if(ownerId != null){
            VehicleOwner owner = ownerRepository.findById(ownerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Owner not found: " + ownerId));
            vehicle.setOwner(owner);
        }
        return vehicleRepository.save(vehicle);
    }
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
}