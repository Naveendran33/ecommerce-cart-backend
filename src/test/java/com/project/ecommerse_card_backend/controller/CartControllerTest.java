package com.project.ecommerse_card_backend.controller;

import com.project.ecommerse_card_backend.dto.cartdto.AddToCartRequest;
import com.project.ecommerse_card_backend.dto.cartdto.CartResponse;
import com.project.ecommerse_card_backend.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    // ==========================================
    // getCart() — Positive
    // ==========================================
    @Test
    void testGetCart_Success() {
        CartResponse cartResponse = new CartResponse(1L, new ArrayList<>(), null);
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        when(cartService.getUserCartResponse(1L)).thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.getCart(httpRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    // ==========================================
    // addToCart() — Positive
    // ==========================================
    @Test
    void testAddToCart_Success() {
        CartResponse cartResponse = new CartResponse(1L, new ArrayList<>(), null);
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        AddToCartRequest addRequest = new AddToCartRequest(100L, 2);

        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        when(cartService.addToCart(any(AddToCartRequest.class), eq(1L))).thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.addToCart(httpRequest, addRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    // ==========================================
    // removeFromCart() — Positive
    // ==========================================
    @Test
    void testRemoveFromCart_Success() {
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        doNothing().when(cartService).removeFromCart(1L, 5L);

        ResponseEntity<Void> response = cartController.removeFromCart(httpRequest, 5L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
    }

    // ==========================================
    // updateCartItemQuantity() — Positive
    // ==========================================
    @Test
    void testUpdateCartItemQuantity_Success() {
        CartResponse cartResponse = new CartResponse(1L, new ArrayList<>(), null);
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        when(cartService.updateCardItemQuantity(1L, 5L, 10)).thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.updateCartItemQuantity(httpRequest, 5L, 10);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }
}
