package com.FB_APP.demo.livraison.responses;

import com.FB_APP.demo.entities.User;
import com.FB_APP.demo.user.dtos.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLivraisonResponse {
    private String localisation ;
    private String time ;
    private String num_tel;
    private UserResponseDto DELIVERY;
    private String product_name ;
    private Date date ;
    private String status ;
}
