package com.project.ecommerse_card_backend.controller;

import com.project.ecommerse_card_backend.dto.orderdto.CheckoutRequest;
import com.project.ecommerse_card_backend.dto.orderdto.OrderResponse;
import com.project.ecommerse_card_backend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import java.util.List;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * REST Controller for managing user orders.
 * Note: The authenticated user's ID is automatically extracted from the JWT token 
 * by the {@link com.project.ecommerse_card_backend.secutiy.jwt.JwtService JwtService} 
 * and passed to these endpoints via the HttpServletRequest attribute "userId".
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(HttpServletRequest request, @Valid @RequestBody CheckoutRequest checkoutRequest) {
        Long userId = (Long) request.getAttribute("userId");
        return new ResponseEntity<>(orderService.checkout(userId, checkoutRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrderHistory(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(orderService.getOrdersForUser(userId));
    }

    @PutMapping("/{orderNumber}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            HttpServletRequest request,
            @PathVariable String orderNumber) {
        Long userId = (Long) request.getAttribute("userId");
        OrderResponse response = orderService.cancelOrder(userId, orderNumber);
        return ResponseEntity.ok(response);
    }
}
