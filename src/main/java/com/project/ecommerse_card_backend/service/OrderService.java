package com.project.ecommerse_card_backend.service;

import com.project.ecommerse_card_backend.Mapper.OrderMapper;
import com.project.ecommerse_card_backend.dto.orderdto.CheckoutRequest;
import com.project.ecommerse_card_backend.dto.orderdto.OrderResponse;
import com.project.ecommerse_card_backend.entity.*;
import com.project.ecommerse_card_backend.enums.OrderStatus;
import com.project.ecommerse_card_backend.exception.*;
import com.project.ecommerse_card_backend.repository.OrderRepository;
import com.project.ecommerse_card_backend.repository.ProductRepository;
import com.project.ecommerse_card_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for handling all order-related business logic.
 * Manages the checkout process, retrieving user orders, and order cancellations.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final CouponService couponService;
    private final ProductRepository productRepository;

    /**
     * Processes a checkout request for a user.
     * Calculates totals, validates stock, applies coupons, creates order records, and clears the cart.
     *
     * @param userId The ID of the user checking out.
     * @param request The checkout payload containing billing, shipping, and coupon details.
     * @return OrderResponse containing the created order details.
     */
    @Transactional
    public OrderResponse checkout(Long userId, CheckoutRequest request){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Cart cart = cartService.getCartForUser(user);

        if(cart.getItems().isEmpty()){
            throw new EmptyCartException("Cart is empty");
        }

        Order order = new Order();

        order.setBillingAddressJson(request.billingAddressJson());
        order.setShippingAddressJson(request.shippingAddressJson());
        order.setCouponCode(request.couponCode());
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal subTotal = BigDecimal.ZERO;

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cart.getItems()){
            Product product = cartItem.getProduct();
            
            // Check if there is enough stock available
            if(product.getStockQuantity() < cartItem.getQuantity()){
                throw new NotHaveStocksException("Product only has the stock of " + product.getStockQuantity());
            }else {
                // Deduct stock immediately upon checkout
                product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());

            }

            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            subTotal = subTotal.add(lineTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(product.getName());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setLineTotal(lineTotal);
            orderItem.setProduct(product);
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setSubTotal(subTotal);
        
        // Apply coupon discount if provided
        if(request.couponCode() != null){
            Coupon coupon = couponService.validateAndGetCoupon(request.couponCode(),subTotal);

            BigDecimal discount = coupon.getDiscountValue();
            order.setDiscountAmount(discount);
            subTotal = subTotal.subtract(discount);
        }else{
            order.setDiscountAmount(BigDecimal.ZERO);
        }
        order.setTaxAmount(BigDecimal.ZERO);
        order.setTotalAmount(subTotal);

        order.setOrderNumber("ORD-" + System.currentTimeMillis());

        cart.getItems().clear();

        orderRepository.save(order);
        return OrderMapper.orderToOrderResponse(order);
    }

    public List<OrderResponse> getOrdersForUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        List<Order> orders = orderRepository.findAllByUserOrderByPlacedAtDesc(user);

        return orders.stream().map(OrderMapper::orderToOrderResponse).toList();
    }

    /**
     * Cancels an order and restores the product stock quantities.
     * Orders can only be cancelled if they haven't been shipped or delivered.
     *
     * @param userId The ID of the user requesting cancellation.
     * @param orderNumber The unique order reference number.
     * @return OrderResponse with the updated CANCELLED status.
     */
    public OrderResponse cancelOrder(Long userId,String orderNumber){
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));

        if(order.getUser().getId().equals(userId)){
            OrderStatus status = order.getStatus();
            if(status.equals(OrderStatus.DELIVERED) || status.equals(OrderStatus.SHIPPED)){
                throw new CannotCancelOrderException("Cannot Cancel the order");
            }else {
                // Restore stock for all products in the cancelled order
                for(OrderItem orderItem : order.getItems()){
                    Product product = orderItem.getProduct();
                    product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());

                    productRepository.save(product);
                }

                order.setStatus(OrderStatus.CANCELLED);

                orderRepository.save(order);
            }
        }else{
            throw new UnAuthorizedAccessException("Unauthorized: You are not Allowed to Cancel this order");
        }

        return OrderMapper.orderToOrderResponse(order);
    }
}
