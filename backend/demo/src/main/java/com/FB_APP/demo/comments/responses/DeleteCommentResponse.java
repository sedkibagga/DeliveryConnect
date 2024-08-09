package com.FB_APP.demo.comments.responses;

import com.FB_APP.demo.entities.Comments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCommentResponse {
    private Integer id ;
    private String commentDeleted; ;
}
