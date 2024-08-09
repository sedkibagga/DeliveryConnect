package com.FB_APP.demo.products.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductByIdDto {
    Optional<String> productName = Optional.empty();
    Optional<String> productPrice = Optional.empty();
    Optional<Integer> quantity = Optional.empty();
}
