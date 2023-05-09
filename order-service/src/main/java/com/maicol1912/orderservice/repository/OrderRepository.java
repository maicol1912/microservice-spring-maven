package com.maicol1912.orderservice.repository;

import com.maicol1912.orderservice.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
