package com.project.ecommerse_card_backend.service;

import com.project.ecommerse_card_backend.entity.Coupon;
import com.project.ecommerse_card_backend.enums.CouponType;
import com.project.ecommerse_card_backend.exception.CouponNotEligibleException;
import com.project.ecommerse_card_backend.exception.InvalidCouponException;
import com.project.ecommerse_card_backend.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    private Coupon validCoupon;

    @BeforeEach
    void setUp() {
        validCoupon = new Coupon();
        validCoupon.setCode("SAVE20");
        validCoupon.setCouponType(CouponType.PERCENTAGE);
        validCoupon.setDiscountValue(new BigDecimal("20"));
        validCoupon.setMinOrderAmount(new BigDecimal("100"));
        validCoupon.setIsActive(true);
    }

    // ==========================================
    // validateAndGetCoupon() — Positive
    // ==========================================
    @Test
    void testValidateAndGetCoupon_Success() {
        when(couponRepository.findByCode("SAVE20")).thenReturn(Optional.of(validCoupon));

        Coupon result = couponService.validateAndGetCoupon("SAVE20", new BigDecimal("150"));

        assertNotNull(result);
        assertEquals("SAVE20", result.getCode());
        verify(couponRepository, times(1)).findByCode("SAVE20");
    }

    // ==========================================
    // validateAndGetCoupon() — Negative: Code not found
    // ==========================================
    @Test
    void testValidateAndGetCoupon_NotFound() {
        when(couponRepository.findByCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(InvalidCouponException.class, () -> {
            couponService.validateAndGetCoupon("INVALID", new BigDecimal("150"));
        });
    }

    // ==========================================
    // validateAndGetCoupon() — Negative: Coupon inactive
    // ==========================================
    @Test
    void testValidateAndGetCoupon_Inactive() {
        validCoupon.setIsActive(false); // Deactivated coupon
        when(couponRepository.findByCode("SAVE20")).thenReturn(Optional.of(validCoupon));

        assertThrows(InvalidCouponException.class, () -> {
            couponService.validateAndGetCoupon("SAVE20", new BigDecimal("150"));
        });
    }

    // ==========================================
    // validateAndGetCoupon() — Negative: Below minimum order amount
    // CouponService throws CouponNotEligibleException (not InvalidCouponException)
    // ==========================================
    @Test
    void testValidateAndGetCoupon_BelowMinimumAmount() {
        when(couponRepository.findByCode("SAVE20")).thenReturn(Optional.of(validCoupon));

        assertThrows(CouponNotEligibleException.class, () -> {
            couponService.validateAndGetCoupon("SAVE20", new BigDecimal("50")); // Min is 100
        });
    }
}
