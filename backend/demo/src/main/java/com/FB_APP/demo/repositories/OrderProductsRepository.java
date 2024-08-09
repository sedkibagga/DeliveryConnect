package com.FB_APP.demo.repositories;

import com.FB_APP.demo.entities.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductsRepository extends JpaRepository<OrderProducts, Integer> {
}
