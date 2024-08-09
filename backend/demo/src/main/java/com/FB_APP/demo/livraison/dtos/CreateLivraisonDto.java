package com.FB_APP.demo.livraison.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLivraisonDto {
    private String localisation ;
    private String time ;
    private String num_tel;
    private String user_name ;
    private Date date ;
    private String product_name ;
}
