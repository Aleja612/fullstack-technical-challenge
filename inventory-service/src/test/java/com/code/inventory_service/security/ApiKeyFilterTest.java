package com.code.inventory_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiKeyFilterTest {

    @InjectMocks
    private ApiKeyFilter apiKeyFilter;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        // Inyectamos la clave correcta
        ReflectionTestUtils.setField(apiKeyFilter, "validApiKey", "test-secret");
    }

    @Test
    void doFilterInternal_ValidKey_ShouldProceed() throws Exception {
        // Arrange: El request trae la key correcta
        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getHeader("X-API-KEY")).thenReturn("test-secret");

        // Act
        apiKeyFilter.doFilterInternal(request, response, filterChain);

        // Assert: Debe llamar a filterChain.doFilter (dejar pasar)
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidKey_ShouldBlock() throws Exception {
        // Arrange: Key incorrecta
        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getHeader("X-API-KEY")).thenReturn("wrong-key");
        
        // Simulamos el writer para que no falle al escribir el error JSON
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        // Act
        apiKeyFilter.doFilterInternal(request, response, filterChain);

        // Assert:
        // 1. NO debe dejar pasar
        verify(filterChain, never()).doFilter(request, response);
        // 2. Debe devolver 401 Unauthorized
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}