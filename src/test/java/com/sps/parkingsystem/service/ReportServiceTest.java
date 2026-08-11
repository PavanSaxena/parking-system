package com.sps.parkingsystem.service;

import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Payment;
import com.sps.parkingsystem.repository.ParkingSlotRepository;
import com.sps.parkingsystem.repository.ParkingTicketRepository;
import com.sps.parkingsystem.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ParkingTicketRepository ticketRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ParkingSlotRepository slotRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void reportServiceReturnsTicketsPaymentsAndRevenue() {
        ParkingTicket ticket = new ParkingTicket();
        Payment payment = new Payment();
        payment.setAmount(80.0);

        when(ticketRepository.findAll()).thenReturn(List.of(ticket));
        when(ticketRepository.findByExitTimeIsNull()).thenReturn(List.of(ticket));
        when(paymentRepository.findAll()).thenReturn(List.of(payment));
        when(paymentRepository.getTotalRevenue()).thenReturn(80.0);

        assertEquals(1, reportService.getAllTickets().size());
        assertEquals(1, reportService.getActiveTickets().size());
        assertEquals(1, reportService.getAllPayments().size());
        assertEquals(80.0, reportService.getTotalRevenue());
    }

    @Test
    void occupancyMethodsHandleZeroAndNonZeroTotals() {
        when(slotRepository.count()).thenReturn(0L);
        when(slotRepository.countByStatus(SlotStatus.OCCUPIED)).thenReturn(0L);
        assertEquals(0.0, reportService.getOccupancyPercentage());

        when(slotRepository.count()).thenReturn(10L);
        when(slotRepository.countByStatus(SlotStatus.OCCUPIED)).thenReturn(4L);
        assertEquals(40.0, reportService.getOccupancyPercentage());
        assertEquals(4L, reportService.getOccupiedSlots());
    }
}
