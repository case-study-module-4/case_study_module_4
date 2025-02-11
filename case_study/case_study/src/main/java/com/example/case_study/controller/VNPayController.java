package com.example.case_study.controller;

import com.example.case_study.service.impl.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;

    // Endpoint tạo URL thanh toán VNPay
    @GetMapping("/vnpay")
    public ResponseEntity<?> createVNPayPayment(@RequestParam BigDecimal amount) {
        String paymentUrl = vnPayService.createPaymentUrl(amount);
        if (paymentUrl != null) {
            return ResponseEntity.ok(Collections.singletonMap("paymentUrl", paymentUrl));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tạo URL thanh toán");
        }
    }

    // Endpoint callback từ VNPay sau khi thanh toán
    @GetMapping("/vnpay-return")
    public ResponseEntity<String> handleVNPayReturn(@RequestParam Map<String, String> params) {
        String vnpResponseCode = params.get("vnp_ResponseCode");
        if ("00".equals(vnpResponseCode)) {
            return ResponseEntity.ok("Thanh toán thành công!");
        } else {
            return ResponseEntity.badRequest().body("Thanh toán thất bại! Response code: " + vnpResponseCode);
        }
    }
}