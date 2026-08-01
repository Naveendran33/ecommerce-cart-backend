package com.project.ecommerse_card_backend.service;

import com.project.ecommerse_card_backend.entity.Coupon;
import com.project.ecommerse_card_backend.exception.CouponNotEligibleException;
import com.project.ecommerse_card_backend.exception.InvalidCouponException;
import com.project.ecommerse_card_backend.repository.CouponRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service responsible for managing and validating discount coupons.
 */
@Service
@AllArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    /**
     * Validates a coupon code against various business rules: existence, active status,
     * expiry dates, and minimum order requirements.
     *
     * @param code The coupon code string to validate.
     * @param subTotal The current cart subtotal to check against minimum order requirements.
     * @return The valid Coupon entity.
     * @throws InvalidCouponException if the coupon is expired, inactive, or not found.
     * @throws CouponNotEligibleException if the order subtotal is too low.
     */
    public Coupon validateAndGetCoupon(String code, BigDecimal subTotal) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new InvalidCouponException("Invalid Coupon"));

        // Check if coupon is active
        if (Boolean.FALSE.equals(coupon.getIsActive())) {
            throw new InvalidCouponException("This coupon is no longer active");
        }

        // Check expiry date
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getValidUntil() != null && coupon.getValidUntil().isBefore(now)) {
            throw new InvalidCouponException("This coupon has expired");
        }

        // Check if coupon validity has started
        if (coupon.getValidFrom() != null && coupon.getValidFrom().isAfter(now)) {
            throw new InvalidCouponException("This coupon is not yet valid");
        }

        // Check minimum order amount
        if (coupon.getMinOrderAmount() != null &&
                subTotal.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new CouponNotEligibleException(
                    "This Coupon is only eligible for Orders above " + coupon.getMinOrderAmount());
        }

        return coupon;
    }
}
