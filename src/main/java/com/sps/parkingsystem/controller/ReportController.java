package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.dto.response.OccupancyResponse;
import com.sps.parkingsystem.dto.response.ParkingTicketResponse;
import com.sps.parkingsystem.dto.response.PaymentResponse;
import com.sps.parkingsystem.mapper.ResponseMapper;
import com.sps.parkingsystem.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/tickets")
    public List<ParkingTicketResponse> getTickets() {
        return reportService.getAllTickets().stream().map(ResponseMapper::toParkingTicketResponse).toList();
    }

    @GetMapping("/active")
    public List<ParkingTicketResponse> getActiveTickets() {
        return reportService.getActiveTickets().stream().map(ResponseMapper::toParkingTicketResponse).toList();
    }

    @GetMapping("/payments")
    public List<PaymentResponse> getAllPayments() {
        return reportService.getAllPayments().stream().map(ResponseMapper::toPaymentResponse).toList();
    }

    @GetMapping("/revenue")
    public Double getTotalRevenue() {
        return reportService.getTotalRevenue();
    }

    @GetMapping("/occupancy")
    public OccupancyResponse getOccupancy() {
        return new OccupancyResponse(
                reportService.getTotalSlots(),
                reportService.getOccupiedSlots(),
                reportService.getOccupancyPercentage()
        );
    }
}
