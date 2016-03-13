package com.pankaj.cube26_paymentportal.Entities;

import java.io.Serializable;

/**
 * Created by Pankaj on 13/03/16.
 */
public class GatewayInfo implements Serializable{
    PaymentGateways[] payment_gateways;

    public PaymentGateways[] getPayment_gateways() {
        return payment_gateways;
    }

    public void setPayment_gateways(PaymentGateways[] payment_gateways) {
        this.payment_gateways = payment_gateways;
    }
}
