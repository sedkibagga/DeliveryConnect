package com.FB_APP.demo.orders;

import com.FB_APP.demo.orders.dtos.AddOrderDto;
import com.FB_APP.demo.orders.dtos.EditOrderDto;
import com.FB_APP.demo.orders.responses.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;
    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    @PostMapping("/addOrder")
    public AddOrderResponse addOrder(@RequestBody AddOrderDto addOrderDto) {
        return ordersService.addOrder(addOrderDto);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SEDENTARY')")
    @GetMapping("/getOrderById/{id}")
    public GetOrderByIdResponse getOrderById(@PathVariable Integer id) {
        return ordersService.getOrderById(id);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SEDENTARY')")
    @GetMapping("/getOrderContainingClientName/{clientName}")
    public List<GetOrderContainingClientNameResponse> getOrderContainingClientName(@PathVariable String clientName) {
        logger.info("clientName: {}", clientName);
        return ordersService.getOrderContainingClientName(clientName);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SEDENTARY')")
    @GetMapping("/getAllOrders")
    public List<GetAllOrdersResponse> getAllOrders() {
        return ordersService.getAllOrders();
    }
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    @GetMapping("getOrdersOfClient")
    public List<GetOrdersOfClientResponse> getOrdersOfClient() {
        return ordersService.getOrdersOfClient();
    }

    @PatchMapping("/editOrder/{id}/{productId}")
    public EditOrderResponse editOrder(@PathVariable Integer id, @RequestBody EditOrderDto editOrderDto , @PathVariable Integer productId) {
        return ordersService.editOrder(id, editOrderDto , productId);
    }

}
