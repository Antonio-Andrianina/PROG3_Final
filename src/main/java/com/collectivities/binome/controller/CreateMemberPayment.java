package com.collectivities.binome.controller;

import lombok.Data;

@Data
public class CreateMemberPayment {
    private Integer amount;
    private String membershipFeeIdentifier;
    private String accountCreditedIdentifier;
    private PaymentMode paymentMode;
}