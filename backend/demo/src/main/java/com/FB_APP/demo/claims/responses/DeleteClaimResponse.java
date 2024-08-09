package com.FB_APP.demo.claims.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteClaimResponse {
    private Integer id ;
    private String claim ;
}

