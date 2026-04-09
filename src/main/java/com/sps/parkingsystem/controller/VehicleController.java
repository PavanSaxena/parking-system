package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.dto.CreateVehicleRequest;
import com.sps.parkingsystem.dto.response.VehicleResponse;
import com.sps.parkingsystem.mapper.ResponseMapper;
import com.sps.parkingsystem.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public VehicleResponse createVehicle(@Valid @RequestBody CreateVehicleRequest request) {
        return ResponseMapper.toVehicleResponse(
                vehicleService.createVehicle(request.getVehicleNumber(), request.getVehicleType(), request.getOwnerId())
        );
    }

    @GetMapping
    public List<VehicleResponse> getAllVehicles() {
        return vehicleService.getAllVehicles().stream().map(ResponseMapper::toVehicleResponse).toList();
    }
}
