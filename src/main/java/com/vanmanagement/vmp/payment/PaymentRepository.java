package com.vanmanagement.vmp.payment;

import com.vanmanagement.vmp.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    PaymentEntity save(PaymentEntity paymentEntity);
}
