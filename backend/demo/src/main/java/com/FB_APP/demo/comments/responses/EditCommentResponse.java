package com.FB_APP.demo.comments.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditCommentResponse {
    private  Integer id ;
    private String comments ;
}
