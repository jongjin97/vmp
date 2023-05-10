package com.vanmanagement.vmp.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private boolean success;
    private String status;
    private String receipt_url;
    private String pg_type;
    private String pg_tid;
    private String pg_provider;
    private String pay_method;
    private long paid_at;
    private int paid_amount;
    private String name;
    private String merchant_uid;
    private String imp_uid;
    private String currency;
    private int card_quota;
    private String card_number;
    private String card_name;
    private String buyer_tel;
    private String buyer_postcode;
    private String buyer_name;
    private String buyer_email;
    private String buyer_addr;
    private String bank_name;
    private String apply_num;
    private Long user_seq;

}
