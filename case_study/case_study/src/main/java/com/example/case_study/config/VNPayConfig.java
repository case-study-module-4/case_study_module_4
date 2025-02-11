package com.example.case_study.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VNPayConfig {
    @Value("${vnpay.tmnCode}")
    private String tmnCode;

    @Value("${vnpay.hashSecret}")
    private String hashSecret;

    @Value("${vnpay.payUrl}")
    private String payUrl;

    @Value("${vnpay.returnUrl}")
    private String returnUrl;

    public String getTmnCode() {
        return tmnCode;
    }

    public String getHashSecret() {
        return hashSecret;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    @PostConstruct
    public void init() {
        System.out.println("VNPay Config Loaded:");
        System.out.println("TmnCode: " + tmnCode);
        System.out.println("HashSecret: " + hashSecret);
        System.out.println("PayUrl: " + payUrl);
        System.out.println("ReturnUrl: " + returnUrl);
    }
}