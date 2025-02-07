package com.example.case_study.controller;

import com.example.case_study.model.User;
import com.example.case_study.service.IPayService;
import com.example.case_study.service.impl.UserService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.Map;

@Controller
public class PaypalController {
    @Autowired
    private IPayService iPayService;

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
            BigDecimal exchangeRate = iPayService.getExchangeRate();

            // Kiểm tra nếu exchangeRate <= 0, báo lỗi
            if (exchangeRate == null || exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
                return "redirect:/deposit?error=exchange_rate_invalid";
            }

            BigDecimal amountUSD = new BigDecimal(amount).divide(exchangeRate, 2, RoundingMode.HALF_UP);

            // Kiểm tra nếu amountUSD < 0.01 (số tiền tối thiểu của PayPal)
            if (amountUSD.compareTo(new BigDecimal("0.01")) < 0) {
                return "redirect:/deposit?error=amount_too_small";
            }

            Payment payment = iPayService.createPaymentWithPayPal(amountUSD, "USD", "paypal",
                    "sale", "Nạp tiền vào tài khoản", CANCEL_URL, SUCCESS_URL);

            for (com.paypal.api.payments.Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
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
            if (payment.getState().equals("approved")) {
                // Lấy số tiền từ giao dịch (đơn vị USD)
                BigDecimal amountUSD = new BigDecimal(payment.getTransactions().get(0).getAmount().getTotal());
                // Lấy tỷ giá thực tế từ API
                BigDecimal exchangeRate = iPayService.getExchangeRate();
                // Chuyển đổi USD -> VND
                BigDecimal amountVND = amountUSD.multiply(exchangeRate).setScale(0, BigDecimal.ROUND_HALF_UP);

                //Cập nhật số dư người dùng
                String username = principal.getName();
                User user = userService.findUserByUsername(username);
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
