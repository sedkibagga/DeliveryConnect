package com.FB_APP.demo.orders.dtos;

import com.FB_APP.demo.entities.OrderProducts;
import com.FB_APP.demo.entities.Products;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditOrderDto {
    private Optional<String> location = Optional.empty();
    private Optional<Date> date = Optional.empty();
    private Optional<String> time = Optional.empty();
    private Optional<Integer> newProductId = Optional.empty();

}
