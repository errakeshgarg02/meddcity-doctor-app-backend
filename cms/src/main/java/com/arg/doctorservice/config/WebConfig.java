package com.arg.doctorservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.arg.doctorservice.converter.DoctorToCreateUserRequestConverter;
import com.arg.doctorservice.converter.SignupRequestToDoctorConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SignupRequestToDoctorConverter());
        registry.addConverter(new DoctorToCreateUserRequestConverter());
    }
}
