package com.example.case_study.service.impl;

import com.example.case_study.config.VNPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VNPayService {
    private final VNPayConfig vnPayConfig;

    @Autowired
    public VNPayService(VNPayConfig vnPayConfig) {
        this.vnPayConfig = vnPayConfig;
    }

    public String createPaymentUrl(BigDecimal amount) {
        try {
            String vnpVersion = "2.1.0";
            String vnpCommand = "pay";
            String orderType = "other";
            String vnpTxnRef = UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 10);
            String vnpIpAddr = "116.110.215.163";
            String vnpCurrCode = "VND";
            String amountStr = amount.multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.HALF_UP)
                    .toBigInteger().toString();

            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", vnpVersion);
            vnpParams.put("vnp_Command", vnpCommand);
            vnpParams.put("vnp_TmnCode", vnPayConfig.getTmnCode());
            vnpParams.put("vnp_Amount", amountStr);
            vnpParams.put("vnp_CurrCode", vnpCurrCode);
            vnpParams.put("vnp_TxnRef", vnpTxnRef);
            vnpParams.put("vnp_OrderInfo", "Nap tien vao tai khoan");
            vnpParams.put("vnp_OrderType", orderType);
            vnpParams.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
            vnpParams.put("vnp_IpAddr", vnpIpAddr);

            String vnpCreateDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            vnpParams.put("vnp_CreateDate", vnpCreateDate);

            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String value = vnpParams.get(fieldName);
                if (value != null && !value.isEmpty()) {
                    hashData.append(fieldName)
                            .append('=')
                            .append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()))
                            .append('&');
                    query.append(fieldName)
                            .append('=')
                            .append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()))
                            .append('&');
                }
            }

            if (hashData.length() > 0) {
                hashData.deleteCharAt(hashData.length() - 1);
            }
            if (query.length() > 0) {
                query.deleteCharAt(query.length() - 1);
            }

            String vnpSecureHash = hmacSHA256(vnPayConfig.getHashSecret(), hashData.toString());
            query.append("&vnp_SecureHash=").append(vnpSecureHash);

            return vnPayConfig.getPayUrl() + "?" + query.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String hmacSHA256(String key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKey);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}