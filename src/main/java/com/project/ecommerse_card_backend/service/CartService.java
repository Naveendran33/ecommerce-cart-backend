package com.project.ecommerse_card_backend.service;

import com.project.ecommerse_card_backend.Mapper.CartMapper;
import com.project.ecommerse_card_backend.dto.cartdto.AddToCartRequest;
import com.project.ecommerse_card_backend.dto.cartdto.CartResponse;
import com.project.ecommerse_card_backend.entity.Cart;
import com.project.ecommerse_card_backend.entity.CartItem;
import com.project.ecommerse_card_backend.entity.Product;
import com.project.ecommerse_card_backend.entity.User;
import com.project.ecommerse_card_backend.exception.ResourceNotFoundException;
import com.project.ecommerse_card_backend.repository.CartItemRepository;
import com.project.ecommerse_card_backend.repository.CartRepository;
import com.project.ecommerse_card_backend.repository.ProductRepository;
import com.project.ecommerse_card_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing user shopping carts.
 * Handles adding, removing, and updating item quantities in the cart.
 */
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;



    /**
     * Adds a product to the user's cart. If the product is already in the cart,
     * it increments the quantity. Otherwise, it creates a new CartItem.
     *
     * @param request The request containing the productId and quantity to add.
     * @param userId The ID of the authenticated user.
     * @return CartResponse reflecting the updated cart state.
     */
    @Transactional
    public CartResponse addToCart(AddToCartRequest request,Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Cart cart = getCartForUser(user);
        List<CartItem> cartItems = cart.getItems();
        Product product = productRepository.findById(request.productId()).orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
        boolean isCardItemFound = false;
        
        // Search if item already exists in cart to update quantity
        for(CartItem cartItem : cartItems){
            if(cartItem.getProduct().equals(product)){
                cartItem.setQuantity(cartItem.getQuantity()+request.quantity());
                isCardItemFound = true;
                break;
            }
        }

        // If not found, add it as a new item to the cart
        if(!isCardItemFound){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(request.quantity());
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }

        cartRepository.save(cart);

        return CartMapper.cartToCartResponse(cart);

    }

    public CartResponse getUserCartResponse(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        return CartMapper.cartToCartResponse(getCartForUser(user));
    }

    @Transactional
    public Void removeFromCart(Long userId,Long cartItemId){
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("There is Nothing in your card"));

        List<CartItem> cartItems = cart.getItems();

        cartItems.removeIf(item -> item.getId().equals(cartItemId));

        cartRepository.save(cart);
        return null;
    }

    @Transactional
    public CartResponse updateCardItemQuantity(Long userId,Long cartItemId,Integer newQuanity){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Cart cart = getCartForUser(user);

        boolean itemExist = false;
        for(CartItem item : cart.getItems()){
            if(item.getId().equals(cartItemId)){
                item.setQuantity(newQuanity);
                itemExist = true;
                break;
            }
        }
        if(!itemExist){
            throw new ResourceNotFoundException("Item Not Found Try to Add the item to card");
        }

        cartRepository.save(cart);
        return CartMapper.cartToCartResponse(cart);
    }


    /**
     * Retrieves the cart for a user. If the user doesn't have a cart, 
     * a new empty cart is initialized and saved automatically.
     */
    public Cart getCartForUser(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });
    }




}
