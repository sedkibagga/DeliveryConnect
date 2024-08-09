package com.FB_APP.demo.repositories;

import com.FB_APP.demo.entities.Client;
import com.FB_APP.demo.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByClient(Client client);
}
