package com.sps.parkingsystem.mapper;

import com.sps.parkingsystem.dto.response.ParkingRateResponse;
import com.sps.parkingsystem.dto.response.ParkingTicketResponse;
import com.sps.parkingsystem.dto.response.PaymentResponse;
import com.sps.parkingsystem.dto.response.SlotResponse;
import com.sps.parkingsystem.dto.response.VehicleResponse;
import com.sps.parkingsystem.enums.PaymentStatus;
import com.sps.parkingsystem.model.ParkingRate;
import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Payment;
import com.sps.parkingsystem.model.Vehicle;

public final class ResponseMapper {

    private ResponseMapper() {
    }

    public static ParkingTicketResponse toParkingTicketResponse(ParkingTicket ticket) {
        PaymentStatus paymentStatus = PaymentStatus.PENDING;
        if (ticket.getPayment() != null && ticket.getPayment().getPaymentStatus() != null) {
            paymentStatus = ticket.getPayment().getPaymentStatus();
        }

        return new ParkingTicketResponse(
                ticket.getTicketId(),
                ticket.getVehicle() != null ? ticket.getVehicle().getVehicleNumber() : null,
                ticket.getSlot() != null ? ticket.getSlot().getSlotId() : null,
                ticket.getEntryTime(),
                ticket.getExitTime(),
                paymentStatus
        );
    }

    public static SlotResponse toSlotResponse(ParkingSlot slot) {
        return new SlotResponse(slot.getSlotId(), slot.getSlotType(), slot.getStatus());
    }

    public static PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getTicket() != null ? payment.getTicket().getTicketId() : null,
                payment.getAmount(),
                payment.getPaymentStatus(),
                payment.getPaymentTime()
        );
    }

    public static VehicleResponse toVehicleResponse(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getVehicleNumber(),
                vehicle.getVehicleType(),
                vehicle.getOwner() != null ? vehicle.getOwner().getUserId() : null
        );
    }

    public static ParkingRateResponse toParkingRateResponse(ParkingRate rate) {
        return new ParkingRateResponse(rate.getRateId(), rate.getVehicleType(), rate.getHourlyRate());
    }
}



