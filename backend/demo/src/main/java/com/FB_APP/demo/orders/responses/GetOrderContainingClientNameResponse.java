package com.FB_APP.demo.orders.responses;

import com.FB_APP.demo.entities.Client;
import com.FB_APP.demo.entities.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderContainingClientNameResponse {
    private String clientName ;
    private List<Orders> orders ;
}
