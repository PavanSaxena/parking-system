package com.sps.parkingsystem.repository;

import com.sps.parkingsystem.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = 'COMPLETED'")
    Double getTotalRevenue();
    java.util.Optional<Payment> findByTicketTicketId(String ticketId);
}