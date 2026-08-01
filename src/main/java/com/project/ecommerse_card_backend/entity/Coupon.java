package com.project.ecommerse_card_backend.entity;

import com.project.ecommerse_card_backend.enums.CouponType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType couponType;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal discountValue;

    @Column(precision = 10,scale = 2)
    private BigDecimal minOrderAmount;

    @Column(precision = 10,scale = 2)
    private BigDecimal maxDiscountAmount;

    private Integer usageLimit;

    private Integer timesUsed = 0;

    private LocalDateTime validFrom;

    private LocalDateTime validUntil;

    private Boolean isActive = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

}

