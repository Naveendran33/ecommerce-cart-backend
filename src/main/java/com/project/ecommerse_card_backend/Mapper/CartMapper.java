package com.project.ecommerse_card_backend.Mapper;

import com.project.ecommerse_card_backend.dto.cartdto.CartItemResponse;
import com.project.ecommerse_card_backend.dto.cartdto.CartResponse;
import com.project.ecommerse_card_backend.entity.Cart;
import com.project.ecommerse_card_backend.entity.CartItem;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartItemResponse cartItemToCartItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                ProductMapper.productToProductResponse(item.getProduct()),
                item.getQuantity()
        );
    }

    public static CartResponse cartToCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems() != null
                ? cart.getItems().stream()
                .map(CartMapper::cartItemToCartItemResponse)
                .collect(Collectors.toList())
                : Collections.emptyList();

        return new CartResponse(
                cart.getId(),
                itemResponses,
                cart.getCouponCode()
        );
    }
}
