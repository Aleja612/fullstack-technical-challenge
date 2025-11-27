package com.code.product_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter @Setter
public class AppConfig {
    private String apiKey; // Esto mapea automáticamente 'app.api-key'
}