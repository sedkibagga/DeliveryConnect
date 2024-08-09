package com.FB_APP.demo.comments.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserCommentsDto {
    private Integer id ;
    private String name ;
    private String email ;
    private String role  ;

}
