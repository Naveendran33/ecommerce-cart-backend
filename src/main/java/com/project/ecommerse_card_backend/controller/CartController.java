package com.project.ecommerse_card_backend.controller;

import com.project.ecommerse_card_backend.dto.cartdto.AddToCartRequest;
import com.project.ecommerse_card_backend.dto.cartdto.CartResponse;
import com.project.ecommerse_card_backend.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(cartService.getUserCartResponse(userId));
    }

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(HttpServletRequest request, @Valid @RequestBody AddToCartRequest addToCartRequest) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(cartService.addToCart(addToCartRequest, userId));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(HttpServletRequest request, @PathVariable Long cartItemId) {
        Long userId = (Long) request.getAttribute("userId");
        cartService.removeFromCart(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItemQuantity(
            HttpServletRequest request, 
            @PathVariable Long cartItemId, 
            @RequestParam Integer quantity) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(cartService.updateCardItemQuantity(userId, cartItemId, quantity));
    }
}
