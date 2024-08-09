package com.FB_APP.demo.repositories;

import com.FB_APP.demo.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Integer> {
    List<Products> findByProductNameContaining(String productName);
}
