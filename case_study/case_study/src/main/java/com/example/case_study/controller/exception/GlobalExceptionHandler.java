package com.example.case_study.controller.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        logger.error("RuntimeException xảy ra: ", ex);
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/error";
    }
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        logger.error("Lỗi không xác định: ", ex);
        model.addAttribute("errorMessage", "Đã xảy ra lỗi: " + ex.getMessage());
        return "error/error";
    }
}
