package com.vanmanagement.vmp.controller;

import com.vanmanagement.vmp.payment.Payment;
import com.vanmanagement.vmp.payment.PaymentEntity;
import com.vanmanagement.vmp.payment.PaymentResponse;
import com.vanmanagement.vmp.payment.PaymentService;
import com.vanmanagement.vmp.jwt.JwtAuthentication;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

import static com.vanmanagement.vmp.utils.ApiUtils.ApiResult;
import static com.vanmanagement.vmp.utils.ApiUtils.success;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ApiResult<PaymentResponse> savePayment(@AuthenticationPrincipal JwtAuthentication authentication
            , @RequestBody Payment payment) throws AccountNotFoundException {
        ModelMapper modelMapper = new ModelMapper();
        PaymentEntity paymentEntity = paymentService.savePayment(payment, authentication.id);
        PaymentResponse paymentResponse = modelMapper.map(paymentEntity, PaymentResponse.class);

        return success(paymentResponse);
    }
}
