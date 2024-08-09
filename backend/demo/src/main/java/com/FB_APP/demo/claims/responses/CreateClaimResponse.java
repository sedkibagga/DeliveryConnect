package com.FB_APP.demo.claims.responses;

import com.FB_APP.demo.entities.Client;
import com.FB_APP.demo.user.dtos.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClaimResponse {
    private Integer id ;
    private String claim ;
    private Client client;
    private UserResponseDto user ;
}
