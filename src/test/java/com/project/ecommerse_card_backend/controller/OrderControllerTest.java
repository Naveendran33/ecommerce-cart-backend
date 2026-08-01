package com.project.ecommerse_card_backend.controller;

import com.project.ecommerse_card_backend.dto.orderdto.CheckoutRequest;
import com.project.ecommerse_card_backend.dto.orderdto.OrderResponse;
import com.project.ecommerse_card_backend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    // Helper to build an OrderResponse record
    private OrderResponse buildOrderResponse(String orderNumber, String status) {
        return new OrderResponse(
                1L, orderNumber,
                BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.TEN,
                null, status, "{}", "{}", List.of()
        );
    }

    // ==========================================
    // checkout() — Positive
    // ==========================================
    @Test
    void testCheckout_Success() {
        OrderResponse orderResponse = buildOrderResponse("ORD-123", "PENDING");
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        when(orderService.checkout(eq(1L), any(CheckoutRequest.class))).thenReturn(orderResponse);

        CheckoutRequest checkoutRequest = new CheckoutRequest("{}", "{}", null);
        ResponseEntity<OrderResponse> response = orderController.checkout(httpRequest, checkoutRequest);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertEquals("ORD-123", response.getBody().orderNumber());
    }

    // ==========================================
    // getOrderHistory() — Positive
    // ==========================================
    @Test
    void testGetOrderHistory_Success() {
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        when(orderService.getOrdersForUser(1L)).thenReturn(List.of(buildOrderResponse("ORD-123", "PENDING")));

        ResponseEntity<List<OrderResponse>> response = orderController.getOrderHistory(httpRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    // ==========================================
    // cancelOrder() — Positive
    // ==========================================
    @Test
    void testCancelOrder_Success() {
        OrderResponse orderResponse = buildOrderResponse("ORD-123", "CANCELLED");
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        when(orderService.cancelOrder(1L, "ORD-123")).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderController.cancelOrder(httpRequest, "ORD-123");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("CANCELLED", response.getBody().status());
    }
}
