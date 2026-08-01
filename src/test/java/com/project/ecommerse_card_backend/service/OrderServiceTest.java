package com.project.ecommerse_card_backend.service;

import com.project.ecommerse_card_backend.dto.orderdto.CheckoutRequest;
import com.project.ecommerse_card_backend.dto.orderdto.OrderResponse;
import com.project.ecommerse_card_backend.entity.*;
import com.project.ecommerse_card_backend.enums.OrderStatus;
import com.project.ecommerse_card_backend.exception.*;
import com.project.ecommerse_card_backend.repository.OrderRepository;
import com.project.ecommerse_card_backend.repository.ProductRepository;
import com.project.ecommerse_card_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CouponService couponService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private User testUser;
    private Cart testCart;
    private Product testProduct;
    private CartItem testCartItem;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testProduct = new Product();
        testProduct.setId(100L);
        testProduct.setName("Laptop");
        testProduct.setPrice(new BigDecimal("1000.00"));
        testProduct.setStockQuantity(10);

        testCartItem = new CartItem();
        testCartItem.setProduct(testProduct);
        testCartItem.setQuantity(2);

        testCart = new Cart();
        testCart.setId(10L);
        testCart.setUser(testUser);
        List<CartItem> items = new ArrayList<>();
        items.add(testCartItem);
        testCart.setItems(items);

        OrderItem oi = new OrderItem();
        oi.setProduct(testProduct);
        oi.setQuantity(1);

        testOrder = new Order();
        testOrder.setOrderNumber("ORD-123");
        testOrder.setUser(testUser);
        testOrder.setStatus(OrderStatus.PENDING);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(oi);
        testOrder.setItems(orderItems);
    }

    // ==========================================
    // checkout() — Positive
    // ==========================================
    @Test
    void testCheckout_Success() {
        CheckoutRequest request = new CheckoutRequest("{}", "{}", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartService.getCartForUser(testUser)).thenReturn(testCart);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        OrderResponse response = orderService.checkout(1L, request);

        assertNotNull(response);
        assertEquals(8, testProduct.getStockQuantity()); // 10 - 2
        assertTrue(testCart.getItems().isEmpty());       // Cart cleared
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    // ==========================================
    // checkout() — Negative: Empty cart
    // ==========================================
    @Test
    void testCheckout_EmptyCart() {
        testCart.getItems().clear();
        CheckoutRequest request = new CheckoutRequest("{}", "{}", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartService.getCartForUser(testUser)).thenReturn(testCart);

        assertThrows(EmptyCartException.class, () -> orderService.checkout(1L, request));
    }

    // ==========================================
    // checkout() — Negative: Not enough stock
    // ==========================================
    @Test
    void testCheckout_NotEnoughStock() {
        testCartItem.setQuantity(50); // Only 10 in stock
        CheckoutRequest request = new CheckoutRequest("{}", "{}", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartService.getCartForUser(testUser)).thenReturn(testCart);

        assertThrows(NotHaveStocksException.class, () -> orderService.checkout(1L, request));
    }

    // ==========================================
    // getOrdersForUser() — Positive
    // ==========================================
    @Test
    void testGetOrdersForUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(orderRepository.findAllByUserOrderByPlacedAtDesc(testUser)).thenReturn(List.of(testOrder));

        List<OrderResponse> responses = orderService.getOrdersForUser(1L);

        assertEquals(1, responses.size());
        assertEquals("ORD-123", responses.get(0).orderNumber()); // record accessor
    }

    // ==========================================
    // cancelOrder() — Positive
    // ==========================================
    @Test
    void testCancelOrder_Success() {
        when(orderRepository.findByOrderNumber("ORD-123")).thenReturn(Optional.of(testOrder));

        OrderResponse response = orderService.cancelOrder(1L, "ORD-123");

        assertEquals(OrderStatus.CANCELLED.name(), response.status()); // record accessor
        assertEquals(11, testProduct.getStockQuantity()); // 10 + 1 restored
        verify(productRepository, times(1)).save(testProduct);
        verify(orderRepository, times(1)).save(testOrder);
    }

    // ==========================================
    // cancelOrder() — Negative: Already shipped
    // ==========================================
    @Test
    void testCancelOrder_AlreadyShipped() {
        testOrder.setStatus(OrderStatus.SHIPPED);
        when(orderRepository.findByOrderNumber("ORD-123")).thenReturn(Optional.of(testOrder));

        assertThrows(CannotCancelOrderException.class, () ->
                orderService.cancelOrder(1L, "ORD-123"));
    }

    // ==========================================
    // cancelOrder() — Negative: Unauthorized user
    // ==========================================
    @Test
    void testCancelOrder_UnauthorizedUser() {
        when(orderRepository.findByOrderNumber("ORD-123")).thenReturn(Optional.of(testOrder));

        assertThrows(UnAuthorizedAccessException.class, () ->
                orderService.cancelOrder(99L, "ORD-123")); // wrong user
    }

    // ==========================================
    // cancelOrder() — Negative: Order not found
    // ==========================================
    @Test
    void testCancelOrder_OrderNotFound() {
        when(orderRepository.findByOrderNumber("ORD-999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                orderService.cancelOrder(1L, "ORD-999"));
    }
}
