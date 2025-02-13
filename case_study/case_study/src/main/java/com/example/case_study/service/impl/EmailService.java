package com.example.case_study.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendVerificationEmail(String to, String name, String token) throws MessagingException {
        String subject = "Xác nhận tài khoản của bạn";
        String url = "http://localhost:8080/api/auth/verify?token=" + token;

        // Tạo dữ liệu cho Thymeleaf
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", name);
        variables.put("verificationUrl", url);

        // Render nội dung email từ template
        String htmlContent = renderHtmlTemplate("email/verification-email", variables);

        // Gửi email
        sendHtmlEmail(to, subject, htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private String renderHtmlTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
}
