package com.FB_APP.demo.comments.responses;

import com.FB_APP.demo.entities.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentResponse {
    private Integer id ;
    private Client client ;
    private String comment;
}
