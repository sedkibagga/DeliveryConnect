package com.FB_APP.demo.orders.responses;

import com.FB_APP.demo.entities.Client;
import com.FB_APP.demo.entities.OrderProducts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderByIdResponse {
    private Integer id ;
    private String location;
    private Date date;
    private String time;
    private List<OrderProducts> products ;
}
