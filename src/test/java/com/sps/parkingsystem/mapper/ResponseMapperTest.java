package com.sps.parkingsystem.mapper;

import com.sps.parkingsystem.enums.PaymentStatus;
import com.sps.parkingsystem.model.ParkingRate;
import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Payment;
import com.sps.parkingsystem.model.Vehicle;
import com.sps.parkingsystem.model.VehicleOwner;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResponseMapperTest {

    @Test
    void mapsTicketWithPaymentStatusAndNulls() {
        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId("T1");
        ticket.setEntryTime(LocalDateTime.of(2024, 1, 1, 10, 0));
        ticket.setExitTime(LocalDateTime.of(2024, 1, 1, 12, 0));

        var response = ResponseMapper.toParkingTicketResponse(ticket);

        assertEquals("T1", response.getTicketId());
        assertEquals(PaymentStatus.PENDING, response.getPaymentStatus());
    }

    @Test
    void mapsPaymentVehicleAndRateResponses() {
        Payment payment = new Payment();
        payment.setPaymentId("P1");
        payment.setAmount(10.5);
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentTime(LocalDateTime.of(2024, 1, 1, 13, 0));

        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId("T1");
        payment.setTicket(ticket);

        VehicleOwner owner = new VehicleOwner();
        owner.setUserId("OWNER-1");
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA01");
        vehicle.setVehicleType("CAR");
        vehicle.setOwner(owner);

        ParkingSlot slot = new ParkingSlot("S1", "CAR", com.sps.parkingsystem.enums.SlotStatus.AVAILABLE);
        ParkingRate rate = new ParkingRate();
        rate.setRateId("R1");
        rate.setVehicleType("CAR");
        rate.setHourlyRate(15.0);

        var paymentResponse = ResponseMapper.toPaymentResponse(payment);
        var vehicleResponse = ResponseMapper.toVehicleResponse(vehicle);
        var slotResponse = ResponseMapper.toSlotResponse(slot);
        var rateResponse = ResponseMapper.toParkingRateResponse(rate);

        assertEquals("P1", paymentResponse.getPaymentId());
        assertEquals("T1", paymentResponse.getTicketId());
        assertEquals("OWNER-1", vehicleResponse.getOwnerId());
        assertEquals("S1", slotResponse.getSlotId());
        assertEquals("R1", rateResponse.getRateId());
        assertNotNull(slotResponse.getStatus());
    }
}
