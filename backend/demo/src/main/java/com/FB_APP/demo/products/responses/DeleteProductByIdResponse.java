package com.FB_APP.demo.products.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProductByIdResponse {
    private Integer id ;
    private String productNameDeleted ;
    private String productPrice ;
    private Integer quantity ;
}
