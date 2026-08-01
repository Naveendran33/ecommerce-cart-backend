package com.project.ecommerse_card_backend.controller;

import com.project.ecommerse_card_backend.entity.Coupon;
import com.project.ecommerse_card_backend.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponRepository couponRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        coupon.setCreatedAt(java.time.LocalDateTime.now());
        Coupon savedCoupon = couponRepository.save(coupon);
        return new ResponseEntity<>(savedCoupon, HttpStatus.CREATED);
    }
}
