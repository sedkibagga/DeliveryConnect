package com.FB_APP.demo.claims.responses;

import com.FB_APP.demo.claims.dtos.ClientDto;
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
public class UpdateClaimResponse {
    private ClientDto client ;
    private UserResponseDto user ;
}
