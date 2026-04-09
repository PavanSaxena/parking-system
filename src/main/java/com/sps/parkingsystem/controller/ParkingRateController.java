package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.dto.CreateRateRequest;
import com.sps.parkingsystem.dto.response.ParkingRateResponse;
import com.sps.parkingsystem.mapper.ResponseMapper;
import com.sps.parkingsystem.service.ParkingRateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rates")
public class ParkingRateController {
    private final ParkingRateService rateService;
    public ParkingRateController(ParkingRateService rateService) {
        this.rateService = rateService;
    }

    @PostMapping
    public ParkingRateResponse createRate(@Valid @RequestBody CreateRateRequest request) {
        return ResponseMapper.toParkingRateResponse(rateService.createRate(
                request.getRateId(),
                request.getVehicleType(),
                request.getHourlyRate()
        ));
    }

    @GetMapping
    public List<ParkingRateResponse> getAllRates() {
        return rateService.getAllRates().stream().map(ResponseMapper::toParkingRateResponse).toList();
    }
}
