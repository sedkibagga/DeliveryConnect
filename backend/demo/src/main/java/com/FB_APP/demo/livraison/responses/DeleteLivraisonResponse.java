package com.FB_APP.demo.livraison.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteLivraisonResponse {
    private Integer id ;
    private String localisation ;
    private String time ;
    private String num_tel ;
    private Date date ;
    private String product_name ;
    private String status ;
}
