package com.FB_APP.demo.orders.dtos;

import com.FB_APP.demo.entities.Products;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddOrderDto {
    private String location;
    private Date date;
    private String time;
    private List<Products> products;
}
