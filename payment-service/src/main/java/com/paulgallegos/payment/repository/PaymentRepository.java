package com.paulgallegos.payment.repository;

import com.paulgallegos.payment.domain.Payment;
import com.paulgallegos.payment.domain.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);

    Page<Payment> findBySenderAccountId(UUID senderAccountId, Pageable pageable);

    Page<Payment> findBySenderAccountIdAndStatus(UUID senderAccountId, PaymentStatus status, Pageable pageable);

}
