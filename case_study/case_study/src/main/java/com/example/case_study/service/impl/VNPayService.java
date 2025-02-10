//package com.example.case_study.service.impl;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//import org.apache.commons.codec.binary.Hex;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.math.BigDecimal;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Service
//public class VNPayService {
//    @Value("${vnpay.tmncode}")
//    private String vnp_TmnCode;
//
//    @Value("${vnpay.hashSecret")
//    private String vnp_HashSecret;
//
//    @Value("${vnpay.payUrl}")
//    private String vnp_Url;
//
//    @Value("${vnpay.returnUrl}")
//    private String vnp_ReturnUrl;
//
//    public String createPaymentUrl(BigDecimal amount, String orderInfo, HttpServletRequest request) {
//        try {
//            Map<String, String> vnp_Params = new HashMap<>();
//            vnp_Params.put("vnp_Version", "2.1.0");
//            vnp_Params.put("vnp_Command", "pay");
//            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//            vnp_Params.put("vnp_Amount", String.valueOf(amount.multiply(BigDecimal.valueOf(100)))); // VNPay dùng đơn vị VND
//            vnp_Params.put("vnp_CurrCode", "VND");
//            vnp_Params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
//            vnp_Params.put("vnp_OrderInfo", orderInfo);
//            vnp_Params.put("vnp_Locale", "vn");
//            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
//            vnp_Params.put("vnp_IpAddr", request.getRemoteAddr());
//            vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
//
//            // Tạo URL
//            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
//            Collections.sort(fieldNames);
//            StringBuilder hashData = new StringBuilder();
//            StringBuilder query = new StringBuilder();
//
//            for (String fieldName : fieldNames) {
//                String value = vnp_Params.get(fieldName);
//                if (value != null && !value.isEmpty()) {
//                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
//                            .append("=")
//                            .append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()))
//                            .append("&");
//                    hashData.append(fieldName).append("=").append(value).append("&");
//                }
//            }
//
//            String queryUrl = query.substring(0, query.length() - 1);
//            String secureHash = hmacSHA512(vnp_HashSecret, hashData.substring(0, hashData.length() - 1));
//            queryUrl += "&vnp_SecureHash=" + secureHash;
//
//            return vnp_Url + "?" + queryUrl;
//        } catch (Exception e) {
//            throw new RuntimeException("Error creating VNPay payment URL", e);
//        }
//    }
//
//    private String hmacSHA512(String key, String data) throws Exception {
//        Mac hmac = Mac.getInstance("HmacSHA512");
//        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
//        hmac.init(secretKey);
//        byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
//        return Hex.encodeHexString(hash);
//    }
//}
