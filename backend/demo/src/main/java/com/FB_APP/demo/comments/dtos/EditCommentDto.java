package com.FB_APP.demo.comments.dtos;

import lombok.*;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditCommentDto {
    private Optional<String> commentUpdated = Optional.empty();
 }
