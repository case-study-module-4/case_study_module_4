package com.example.case_study.service.impl;

import com.example.case_study.service.IPayService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PaypalService implements IPayService {
    @Value("${exchange.rate.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private APIContext apiContext;

    @Override
    public Payment createPaymentWithPayPal(BigDecimal total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền thanh toán phải lớn hơn 0");
        }

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent.toUpperCase());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecution);
    }

    @Override
    public BigDecimal getExchangeRate() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl).toUriString();
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("conversion_rates")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("conversion_rates");
                if (rates.containsKey("USD")) {
                    BigDecimal exchangeRate = new BigDecimal(rates.get("USD").toString());
                    if (exchangeRate.compareTo(BigDecimal.ZERO) > 0) {
                        return exchangeRate;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BigDecimal("25000");
    }
}
