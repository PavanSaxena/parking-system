package com.sps.parkingsystem.service;

import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Payment;
import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.repository.ParkingSlotRepository;
import com.sps.parkingsystem.repository.ParkingTicketRepository;
import com.sps.parkingsystem.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private final ParkingTicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final ParkingSlotRepository slotRepository;

    public ReportService(ParkingTicketRepository ticketRepository,
                         PaymentRepository paymentRepository,
                         ParkingSlotRepository slotRepository) {
        this.ticketRepository = ticketRepository;
        this.paymentRepository = paymentRepository;
        this.slotRepository = slotRepository;
    }
    public List<ParkingTicket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<ParkingTicket> getActiveTickets() {
        return ticketRepository.findByExitTimeIsNull();
    }
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    public Double getTotalRevenue(){
        Double revenue = paymentRepository.getTotalRevenue();
        return revenue == null ? 0.0 : revenue;
    }

    public long getTotalSlots() {
        return slotRepository.count();
    }

    public long getOccupiedSlots() {
        return slotRepository.countByStatus(SlotStatus.OCCUPIED);
    }

    public double getOccupancyPercentage() {
        long total = getTotalSlots();
        if (total == 0) {
            return 0.0;
        }
        return (getOccupiedSlots() * 100.0) / total;
    }
}
