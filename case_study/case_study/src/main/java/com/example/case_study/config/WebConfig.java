package com.example.case_study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/"); // Thêm các file tĩnh từ thư mục
    }
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new Formatter<LocalDate>() {
            private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public LocalDate parse(String text, Locale locale) {
                // Chuyển đổi chuỗi theo định dạng dd/MM/yyyy thành LocalDate
                return LocalDate.parse(text, dateFormatter);
            }

            @Override
            public String print(LocalDate object, Locale locale) {
                // Định dạng LocalDate theo định dạng dd/MM/yyyy để hiển thị ra view
                return object.format(dateFormatter);
            }
        });
    }
}

