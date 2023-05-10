package com.vanmanagement.vmp.payment;

import com.vanmanagement.vmp.users.UserEntity;
import com.vanmanagement.vmp.users.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final UserService userService;

    public PaymentService(PaymentRepository paymentRepository, UserService userService) {
        this.paymentRepository = paymentRepository;
        this.userService = userService;
    }

    @Transactional
    public PaymentEntity savePayment(Payment payment, Long user_seq) throws AccountNotFoundException {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = userService.findById(user_seq)
                .orElseThrow(() -> new AccountNotFoundException("Could not found user for " + user_seq));
        PaymentEntity paymentEntity = modelMapper.map(payment, PaymentEntity.class);
        paymentEntity.setUser(userEntity);
        return paymentRepository.save(paymentEntity);
    }
}
