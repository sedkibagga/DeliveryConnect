package com.FB_APP.demo.client.responses;

import com.FB_APP.demo.entities.Comments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetClientByNameResponse {
    private Integer id ;
    private String clientName ;
    private List<String> products ;
    private List<Comments> comments ;
}
