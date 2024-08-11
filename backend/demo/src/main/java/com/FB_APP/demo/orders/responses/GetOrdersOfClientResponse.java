package com.FB_APP.demo.orders.responses;

import com.FB_APP.demo.entities.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOrdersOfClientResponse {
    private Orders order;
}
