package com.FB_APP.demo.comments;

import com.FB_APP.demo.comments.dtos.CreateCommentDto;
import com.FB_APP.demo.comments.dtos.EditCommentDto;
import com.FB_APP.demo.comments.responses.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentsController {
    private final CommentsService commentsService;
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    @PostMapping("/createComment")
    public CreateCommentResponse createComment(@RequestBody CreateCommentDto createCommentDto) {
        return commentsService.createComment(createCommentDto);
    }

    @GetMapping("/getAllComments")
    public List<GetAllCommentsResponse> getAllComments() {
        return commentsService.getAllComments();
    }

    @GetMapping("/getCommentByName/{commentName}")
    public GetCommentByNameResponse getCommentByName(@PathVariable String commentName) {
        return commentsService.getCommentByName(commentName);
    }

    @PreAuthorize("hasAnyAuthority('CLIENT' , 'ADMIN')")
    @PatchMapping("/editCommentById/{id}")
    public EditCommentResponse editCommentById(@PathVariable Integer id, @RequestBody EditCommentDto editCommentDto) {
        return commentsService.editCommentById(id, editCommentDto);
    }
    @GetMapping("/getCommentContainingName/{name}")
    public List<GetCommentContainingName> getCommentContainingName(@PathVariable String name) {
        return commentsService.getCommentContainingName(name);
    }
   @PreAuthorize("hasAnyAuthority('CLIENT' , 'ADMIN')")
    @DeleteMapping("/deleteCommentById/{id}")
    public DeleteCommentResponse deleteCommentById(@PathVariable Integer id) {
        return commentsService.deleteCommentById(id);
    }
}
