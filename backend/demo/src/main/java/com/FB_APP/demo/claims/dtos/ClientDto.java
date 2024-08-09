package com.FB_APP.demo.claims.dtos;

import com.FB_APP.demo.entities.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Integer id ;
    private Integer userId ;
    private List<Claims> claims ;
}
