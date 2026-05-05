package com.cati.tcc.config.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {

    @Value("${stripe.api.secret.key}")
    private String secretKey;

    @PostConstruct
    public void setup() {
     
        Stripe.apiKey = secretKey;
    }
}
