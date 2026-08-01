package com.project.ecommerse_card_backend.repository;

import com.project.ecommerse_card_backend.entity.Order;
import com.project.ecommerse_card_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserOrderByPlacedAtDesc(User user);


    Optional<Order> findByOrderNumber(String orderNumber);
}
