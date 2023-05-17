package com.vanmanagement.vmp.payment;

import com.vanmanagement.vmp.users.UserEntity;
import com.vanmanagement.vmp.users.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository, UserRepository userService) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userService;
    }

    @Transactional
    public PaymentEntity savePayment(Payment payment, Long user_seq) throws AccountNotFoundException {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = userRepository.findById(user_seq)
                .orElseThrow(() -> new AccountNotFoundException("Could not found user for " + user_seq));
        PaymentEntity paymentEntity = modelMapper.map(payment, PaymentEntity.class);
        paymentEntity.setUser(userEntity);
        return paymentRepository.save(paymentEntity);
    }
}
