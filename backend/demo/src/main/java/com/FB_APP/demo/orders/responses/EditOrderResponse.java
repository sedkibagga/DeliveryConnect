package com.FB_APP.demo.orders.responses;

import com.FB_APP.demo.entities.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditOrderResponse {
    private Client client;
}
