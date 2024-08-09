package com.FB_APP.demo.comments.responses;

import com.FB_APP.demo.comments.dtos.GetUserCommentsDto;
import com.FB_APP.demo.entities.Client;
import com.FB_APP.demo.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentByNameResponse {
    private Client client ;
    private GetUserCommentsDto user ;
}
