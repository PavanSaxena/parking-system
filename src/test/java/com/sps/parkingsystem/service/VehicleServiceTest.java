package com.sps.parkingsystem.service;

import com.sps.parkingsystem.exception.ResourceNotFoundException;
import com.sps.parkingsystem.model.Vehicle;
import com.sps.parkingsystem.model.VehicleOwner;
import com.sps.parkingsystem.repository.VehicleOwnerRepository;
import com.sps.parkingsystem.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleOwnerRepository ownerRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void createVehicleWithoutOwnerPersistsVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA01");
        vehicle.setVehicleType("CAR");
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));

        Vehicle result = vehicleService.createVehicle("KA01", "CAR", null);

        assertEquals("KA01", result.getVehicleNumber());
        assertEquals("CAR", result.getVehicleType());
    }

    @Test
    void createVehicleWithOwnerUsesOwnerRepository() {
        VehicleOwner owner = new VehicleOwner();
        owner.setUserId("OWNER-1");
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA02");
        vehicle.setVehicleType("BIKE");
        when(ownerRepository.findById("OWNER-1")).thenReturn(Optional.of(owner));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));

        Vehicle result = vehicleService.createVehicle("KA02", "BIKE", "OWNER-1");

        assertEquals("OWNER-1", result.getOwner().getUserId());
        verify(ownerRepository).findById("OWNER-1");
    }

    @Test
    void createVehicleThrowsWhenOwnerMissing() {
        when(ownerRepository.findById("OWNER-1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> vehicleService.createVehicle("KA02", "BIKE", "OWNER-1"));
    }

    @Test
    void getAllVehiclesReturnsRepositoryValues() {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA03");
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));

        List<Vehicle> result = vehicleService.getAllVehicles();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
