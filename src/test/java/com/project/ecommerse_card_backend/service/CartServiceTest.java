package com.project.ecommerse_card_backend.service;

import com.project.ecommerse_card_backend.dto.cartdto.AddToCartRequest;
import com.project.ecommerse_card_backend.dto.cartdto.CartResponse;
import com.project.ecommerse_card_backend.entity.Cart;
import com.project.ecommerse_card_backend.entity.CartItem;
import com.project.ecommerse_card_backend.entity.Category;
import com.project.ecommerse_card_backend.entity.Product;
import com.project.ecommerse_card_backend.entity.User;
import com.project.ecommerse_card_backend.exception.ResourceNotFoundException;
import com.project.ecommerse_card_backend.repository.CartItemRepository;
import com.project.ecommerse_card_backend.repository.CartRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    private User testUser;
    private Product testProduct;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        Category testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");
        testCategory.setSlug("electronics");

        testProduct = new Product();
        testProduct.setId(100L);
        testProduct.setStockQuantity(50);
        testProduct.setPrice(new BigDecimal("10.00"));
        testProduct.setCategory(testCategory);

        testCart = new Cart();
        testCart.setId(10L);
        testCart.setUser(testUser);
        testCart.setItems(new ArrayList<>());
    }

    // ==========================================
    // addToCart() — Positive: New item added
    // ==========================================
    @Test
    void testAddToCart_Success_NewItem() {
        AddToCartRequest request = new AddToCartRequest(100L, 2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(100L)).thenReturn(Optional.of(testProduct));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartResponse response = cartService.addToCart(request, 1L);

        assertNotNull(response);
        assertEquals(1, testCart.getItems().size());
        assertEquals(2, testCart.getItems().get(0).getQuantity());
    }

    // ==========================================
    // addToCart() — Negative: Product not found
    // ==========================================
    @Test
    void testAddToCart_ProductNotFound() {
        AddToCartRequest request = new AddToCartRequest(999L, 2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.addToCart(request, 1L);
        });
    }

    // ==========================================
    // removeFromCart() — Positive
    // ==========================================
    @Test
    void testRemoveFromCart_Success() {
        CartItem cartItem = new CartItem();
        cartItem.setId(5L);
        testCart.getItems().add(cartItem);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        cartService.removeFromCart(1L, 5L);

        assertTrue(testCart.getItems().isEmpty());
        verify(cartRepository, times(1)).save(testCart);
    }

    // ==========================================
    // removeFromCart() — Negative: Cart not found
    // ==========================================
    @Test
    void testRemoveFromCart_CartNotFound() {
        when(cartRepository.findByUserId(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.removeFromCart(99L, 5L);
        });
    }

    // ==========================================
    // updateCardItemQuantity() — Positive
    // ==========================================
    @Test
    void testUpdateCartItemQuantity_Success() {
        CartItem cartItem = new CartItem();
        cartItem.setId(5L);
        cartItem.setQuantity(2);
        cartItem.setProduct(testProduct); // product with category needed by CartMapper
        testCart.getItems().add(cartItem);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartResponse response = cartService.updateCardItemQuantity(1L, 5L, 10);

        assertNotNull(response);
        assertEquals(10, cartItem.getQuantity());
    }

    // ==========================================
    // updateCardItemQuantity() — Negative: Item not found
    // ==========================================
    @Test
    void testUpdateCartItemQuantity_ItemNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.updateCardItemQuantity(1L, 99L, 5);
        });
    }

    // ==========================================
    // getCartForUser() — Positive: Creates new cart
    // ==========================================
    @Test
    void testGetCartForUser_CreatesNewCartIfNoneExists() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.getCartForUser(testUser);

        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    // ==========================================
    // getCartForUser() — Positive: Returns existing cart
    // ==========================================
    @Test
    void testGetCartForUser_ReturnsExistingCart() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        Cart result = cartService.getCartForUser(testUser);

        assertNotNull(result);
        assertEquals(testCart.getId(), result.getId());
        verify(cartRepository, never()).save(any(Cart.class));
    }
}
