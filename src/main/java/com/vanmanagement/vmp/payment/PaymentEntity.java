package com.vanmanagement.vmp.payment;

import com.vanmanagement.vmp.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "imp_uid")
    private String imp_uid;

    @Column(name = "merchant_uid")
    private String merchant_uid;

    @Column(name = "name")
    private String name;

    @Column(name = "paid_amount")
    private int paid_amount;

    @Column(name = "paid_at")
    private long paid_at;

    @Column(name = "pay_method")
    private String pay_method;

    @Column(name = "status")
    private String status;

    @Column(name = "success")
    private boolean success;

    @Column(name = "pg_type")
    private String pg_type;

    @Column(name = "pg_tid")
    private String pg_tid;

    @Column(name = "pg_provider")
    private String pg_provider;

    @Column(name = "receipt_url")
    private String receipt_url;

    @Column(name = "currency")
    private String currency;

    @Column(name = "card_quota")
    private int card_quota;

    @Column(name = "card_number")
    private String card_number;

    @Column(name = "card_name")
    private String card_name;

    @Column(name = "buyer_tel")
    private String buyer_tel;

    @Column(name = "buyer_postcode")
    private String buyer_postcode;

    @Column(name = "buyer_name")
    private String buyer_name;

    @Column(name = "buyer_email")
    private String buyer_email;

    @Column(name = "buyer_addr")
    private String buyer_addr;

    @Column(name = "bank_name")
    private String bank_name;

    @Column(name = "apply_num")
    private String apply_num;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private UserEntity user;
}
