package com.FB_APP.demo.repositories;

import com.FB_APP.demo.entities.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductsRepository extends JpaRepository<OrderProducts, Integer> {
    List<OrderProducts> findAllByProductId(Integer productId);

}
