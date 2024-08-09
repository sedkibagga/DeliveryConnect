package com.FB_APP.demo.livraison.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLivraisonDto {
    private Optional<String> localisation = Optional.empty();
    private Optional<String> time = Optional.empty();
    private Optional<String> num_tel = Optional.empty();
    private Optional<String> product_name = Optional.empty();
    private Optional<Date> date = Optional.empty();
}

