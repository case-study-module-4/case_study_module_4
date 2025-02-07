package com.example.case_study.controller;

import com.example.case_study.model.User;
import com.example.case_study.service.impl.PaypalService;
import com.example.case_study.service.impl.UserService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;

@Controller
public class PaypalController {
    @Autowired
    private PaypalService payPalService;

    @Autowired
    private UserService userService;

    @GetMapping("/deposit")
    public String showDepositForm(Model model, Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        model.addAttribute("user", user);
        return "user/deposit"; // Hiển thị trang nạp tiền
    }

    private static final String SUCCESS_URL = "http://localhost:8080/success";
    private static final String CANCEL_URL = "http://localhost:8080/cancel";

    @PostMapping("/paypal")
    public String pay(@RequestParam double amount) {
        try {
            // Chuyển đổi VND sang USD (1 USD = 24,000 VND)
            double amountUSD = amount / 24000.0;

            Payment payment = payPalService.createPaymentWithPayPal(amountUSD, "USD", "paypal",
                    "sale", "Nạp tiền vào tài khoản", CANCEL_URL, SUCCESS_URL);

            for (com.paypal.api.payments.Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref(); // Điều hướng đến PayPal
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/deposit?error"; // Nếu lỗi, quay lại trang nạp tiền
    }

    @GetMapping("/success")
    public String successPay(@RequestParam Map<String, String> params, Principal principal) {
        String paymentId = params.get("paymentId");
        String payerId = params.get("PayerID");
        if (principal == null) {
            return "redirect:/login";
        }
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                // Lấy số tiền từ giao dịch (đơn vị USD)
                BigDecimal amountUSD = new BigDecimal(payment.getTransactions().get(0).getAmount().getTotal());
                // Chuyển đổi USD sang VND
                BigDecimal amountVND = amountUSD.multiply(new BigDecimal("24000"));
                String username = principal.getName();
                User user = userService.findUserByUsername(username);
                // Cập nhật số dư người dùng
                userService.updateUserBalance(user.getId(), amountVND);
                return "user/success";
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "user/success";
    }

    @GetMapping("/cancel")
    public String cancelPay() {
        return "user/cancel";
    }
}
