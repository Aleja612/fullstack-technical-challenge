package com.code.product_service.security;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class ApiKeyFilterTest {

    @InjectMocks
    private ApiKeyFilter apiKeyFilter;

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        // Inyectamos la clave 'secret-key-123' en el filtro
        ReflectionTestUtils.setField(apiKeyFilter, "validApiKey", "secret-key-123");
    }

    @Test
    void doFilterInternal_ValidKey_ShouldProceed() throws Exception {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/products");
        when(request.getHeader("X-API-KEY")).thenReturn("secret-key-123");

        // Act
        apiKeyFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidKey_ShouldReturn401() throws Exception {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/products");
        when(request.getHeader("X-API-KEY")).thenReturn("wrong-key");
        
        // Mock del writer para que no falle al escribir el JSON de error
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        // Act
        apiKeyFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, never()).doFilter(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void doFilterInternal_SwaggerUrl_ShouldProceedWithoutKey() throws Exception {
        // Arrange: Las URLs de swagger deben ser públicas
        when(request.getRequestURI()).thenReturn("/swagger-ui/index.html");

        // Act
        apiKeyFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
    }
}