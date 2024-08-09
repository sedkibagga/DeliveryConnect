package com.FB_APP.demo.user.responses;

import com.FB_APP.demo.entities.Livraison;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUsersResponse {
    private Integer id ;
    private String name ;
    private String email ;
    private String role;
    private List<Livraison> livraisons;
}
