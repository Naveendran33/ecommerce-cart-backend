package com.project.ecommerse_card_backend.Mapper;

import com.project.ecommerse_card_backend.dto.orderdto.OrderItemResponse;
import com.project.ecommerse_card_backend.dto.orderdto.OrderResponse;
import com.project.ecommerse_card_backend.entity.Order;
import com.project.ecommerse_card_backend.entity.OrderItem;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderItemResponse orderItemToOrderItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getLineTotal()
        );
    }

    public static OrderResponse orderToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems() != null
                ? order.getItems().stream()
                .map(OrderMapper::orderItemToOrderItemResponse)
                .collect(Collectors.toList())
                : Collections.emptyList();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getSubTotal(),
                order.getDiscountAmount(),
                order.getTaxAmount(),
                order.getTotalAmount(),
                order.getCouponCode(),
                order.getStatus() != null ? order.getStatus().name() : null,
                order.getShippingAddressJson(),
                order.getBillingAddressJson(),
                itemResponses
        );
    }
}
