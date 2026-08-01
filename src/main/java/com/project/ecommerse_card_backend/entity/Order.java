package com.project.ecommerse_card_backend.entity;

import com.project.ecommerse_card_backend.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String orderNumber;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal subTotal;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal discountAmount = BigDecimal.valueOf(0.00);

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal taxAmount;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal totalAmount;

    private String couponCode;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(columnDefinition = "TEXT")
    private String shippingAddressJson;

    @Column(columnDefinition = "TEXT")
    private String billingAddressJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime placedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

}
