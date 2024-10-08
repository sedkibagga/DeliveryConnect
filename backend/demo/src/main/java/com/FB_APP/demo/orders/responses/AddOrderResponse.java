package com.FB_APP.demo.orders.responses;

import com.FB_APP.demo.orders.dtos.ClientOrderNameDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddOrderResponse {
    private Integer id ;
    private ClientOrderNameDto client;

}
