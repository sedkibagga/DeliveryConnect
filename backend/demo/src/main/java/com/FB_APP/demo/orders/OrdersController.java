package com.FB_APP.demo.orders;

import com.FB_APP.demo.orders.dtos.AddOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    @PostMapping("/addOrder")
    public AddOrderResponse addOrder(@RequestBody AddOrderDto addOrderDto) {
        return ordersService.addOrder(addOrderDto);
    }
}
