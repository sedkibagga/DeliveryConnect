package com.FB_APP.demo.client.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientProductsResponse {
    private Integer clientId;
    private List<String> products ;
}
