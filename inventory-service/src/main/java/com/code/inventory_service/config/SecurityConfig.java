package com.code.inventory_service.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.code.inventory_service.security.ApiKeyFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration(ApiKeyFilter filter) {
        FilterRegistrationBean<ApiKeyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        // Aplica el filtro a todas las URLs de la API
        registration.addUrlPatterns("/api/*");
        registration.setOrder(1); // Se ejecuta primero
        return registration;
    }
}
