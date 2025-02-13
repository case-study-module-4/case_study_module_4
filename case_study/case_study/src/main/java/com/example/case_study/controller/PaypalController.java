package com.example.case_study.controller;

import com.example.case_study.model.Deposit;
import com.example.case_study.model.User;
import com.example.case_study.service.IDepositService;
import com.example.case_study.service.IPayService;
import com.example.case_study.service.impl.UserService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class PaypalController {
    @Autowired
    private IPayService iPayService;

    @Autowired
    private UserService userService;

    @Autowired
    private IDepositService depositService;

    private static final String EXCHANGE_RATE_API = "https://open.er-api.com/v6/latest/USD";
    private static final String SUCCESS_URL = "http://localhost:8080/success";
    private static final String CANCEL_URL = "http://localhost:8080/cancel";

    @GetMapping("/deposit")
    public String showDepositForm(Model model, Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        model.addAttribute("user", user);
        return "user/deposit";
    }

    @PostMapping("/paypal")
    public String pay(@RequestParam double amount) {
        try {
            // Lấy tỷ giá từ API
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(EXCHANGE_RATE_API, String.class);

            JSONObject jsonResponse = new JSONObject(response);
            BigDecimal exchangeRate = BigDecimal.ONE;  // Mặc định là 1

            try {
                if (jsonResponse.has("rates") && jsonResponse.getJSONObject("rates").has("VND")) {
                    exchangeRate = BigDecimal.valueOf(jsonResponse.getJSONObject("rates").getDouble("VND"));
                }
            } catch (JSONException e) {
                System.err.println("Lỗi khi lấy tỷ giá từ JSON: " + e.getMessage());
            }
            if (exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
                return "redirect:/deposit?error=exchange_rate_invalid";
            }

            // Chuyển đổi số tiền từ VND -> USD
            BigDecimal amountUSD = new BigDecimal(amount).divide(exchangeRate, 2, RoundingMode.HALF_UP);
            if (amountUSD.compareTo(new BigDecimal("0.01")) < 0) {
                return "redirect:/deposit?error=amount_too_small";
            }

            // Gọi PayPal API để tạo thanh toán
            Payment payment = iPayService.createPaymentWithPayPal(
                    amountUSD, "USD", "paypal", "sale",
                    "Nạp tiền vào tài khoản", CANCEL_URL, SUCCESS_URL
            );

            for (com.paypal.api.payments.Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/deposit?error=api_failure";
        }
        return "redirect:/deposit?error";
    }

    @GetMapping("/success")
    public String successPay(@RequestParam Map<String, String> params, Principal principal) {
        String paymentId = params.get("paymentId");
        String payerId = params.get("PayerID");

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Payment payment = iPayService.executePayment(paymentId, payerId);
            if ("approved".equals(payment.getState())) {
                // Lấy số tiền từ giao dịch (đơn vị USD)
                BigDecimal amountUSD = new BigDecimal(payment.getTransactions().get(0).getAmount().getTotal());

                // Gọi API lấy tỷ giá
                RestTemplate restTemplate = new RestTemplate();
                String response = restTemplate.getForObject(EXCHANGE_RATE_API, String.class);
                JSONObject jsonResponse = new JSONObject(response);

                if (jsonResponse.has("rates") && jsonResponse.getJSONObject("rates").has("VND")) {
                    double exchangeRate = jsonResponse.getJSONObject("rates").getDouble("VND");
                    // Chuyển đổi từ USD -> VND
                    BigDecimal amountVND = amountUSD.multiply(BigDecimal.valueOf(exchangeRate))
                            .setScale(0, RoundingMode.HALF_UP);

                    // Cập nhật số dư người dùng
                    String username = principal.getName();
                    User user = userService.findUserByUsername(username);
                    userService.updateUserBalance(user.getId(), amountVND);

                    // Lưu giao dịch vào bảng deposit
                    Deposit deposit = new Deposit();
                    deposit.setUser(user);
                    deposit.setAmount(amountVND);
                    deposit.setPaymentDate(LocalDate.now());
                    deposit.setPaymentMethod("Thanh toán bằng PayPal");
                    deposit.setStatus("Completed");
                    deposit.setTransactionId(payment.getId()); // Lưu transaction ID từ PayPal
                    deposit.setPayerEmail(payment.getPayer().getPayerInfo().getEmail());
                    depositService.saveDeposit(deposit);
                    return "user/success";
                } else {
                    throw new RuntimeException("Không tìm thấy tỷ giá VND từ API.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/deposit?error=" + e.getMessage();
        }

        return "redirect:/deposit?error=payment_failed";
    }

    @GetMapping("/cancel")
    public String cancelPay() {
        return "user/cancel";
    }
}
