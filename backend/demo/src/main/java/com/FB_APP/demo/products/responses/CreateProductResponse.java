package com.FB_APP.demo.products.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductResponse {
    private Integer id ;
    private String ProductName ;
    private String ProductPrice ;
    private Integer quantity ;
}
