package com.FB_APP.demo.comments;

import com.FB_APP.demo.comments.dtos.CreateCommentDto;
import com.FB_APP.demo.comments.dtos.EditCommentDto;
import com.FB_APP.demo.comments.dtos.GetUserCommentsDto;
import com.FB_APP.demo.comments.responses.*;
import com.FB_APP.demo.entities.Client;
import com.FB_APP.demo.entities.Comments;
import com.FB_APP.demo.entities.User;
import com.FB_APP.demo.repositories.ClientRepository;
import com.FB_APP.demo.repositories.CommentsRepository;
import com.FB_APP.demo.repositories.UserRepository;
import com.FB_APP.demo.user.dtos.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private static final Logger logger = LoggerFactory.getLogger(CommentsService.class);
    @Transactional
    public CreateCommentResponse createComment(CreateCommentDto createCommentDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                throw new ResponseStatusException(UNAUTHORIZED, "User not authenticated");
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "User not found"));

            if ("CLIENT".equals(user.getRole())) {
                Client client = clientRepository.findByUserId(user.getId()).orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Client not found"));

                Comments comments = Comments.builder()
                        .client(client)
                        .comment(createCommentDto.getComment())
                        .build();

                Comments savedComment = commentsRepository.save(comments);

                client.getComments().add(savedComment);
                clientRepository.save(client);

                return CreateCommentResponse.builder()
                        .id(savedComment.getId())
                        .comment(savedComment.getComment())
                        .client(savedComment.getClient())
                        .build();
            }

            throw new ResponseStatusException(UNAUTHORIZED, "User not authorized");

        } catch (Exception e) {
            logger.error("Error creating comment: {}", e.getMessage());
            throw new RuntimeException("Failed to create comment", e);
        }
    }
    @Transactional
    public List<GetAllCommentsResponse> getAllComments() {
        try {
           List<Client> clients = clientRepository.findAll();
           return clients.stream()
                   .map(client -> {
                       User user = userRepository.findById(client.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
                       GetUserCommentsDto getUserCommentsDto = GetUserCommentsDto.builder()
                               .id(user.getId())
                               .name(user.getName())
                               .email(user.getUsername())
                               .role(user.getRole())
                               .build();
                         return GetAllCommentsResponse.builder()
                                 .client(client)
                                 .user(getUserCommentsDto)
                                 .build();
                   })
                   .collect(Collectors.toList()) ;
        } catch (Exception e) {
            logger.error("Error getting all comments: {}", e.getMessage());
            throw new RuntimeException("Failed to get all comments", e);
        }
    }
    @Transactional
    public GetCommentByNameResponse getCommentByName(String name) {
        try {
            User user = userRepository.findByName(name).orElseThrow(() -> new RuntimeException("User not found"));
            if (!"CLIENT".equals(user.getRole())) {
                throw new RuntimeException("User not authorized because this role is " + user.getRole());
            }
            Client client = clientRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Client not found"));
            GetUserCommentsDto getUserCommentsDto = GetUserCommentsDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getUsername())
                    .role(user.getRole())
                    .build();
            return GetCommentByNameResponse.builder()
                    .client(client)
                    .user(getUserCommentsDto)
                    .build();

        } catch (Exception e) {
            logger.error("Error getting comment by name: {}", e.getMessage());
            throw new RuntimeException("Failed to get comment by name", e);
        }
    }
    @Transactional
    public EditCommentResponse editCommentById (Integer id , EditCommentDto editCommentDto) {
        try {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
              throw new ResponseStatusException(UNAUTHORIZED, "User not authenticated");
          }
          UserDetails userDetails = (UserDetails) authentication.getPrincipal() ;
          User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "User not found"));
          if (!"CLIENT".equals(user.getRole())) {
              throw new ResponseStatusException(UNAUTHORIZED, "User not authorized because this role is " + user.getRole());
          }
          Client client = clientRepository.findByUserId(user.getId()).orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Client not found"));
          Comments comment = commentsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Comment not found"));
          if (client.getComments().contains(comment)) {
                comment.setComment(editCommentDto.getCommentUpdated().orElse(comment.getComment()));
                Comments savedComment = commentsRepository.save(comment);
              return EditCommentResponse.builder()
                      .id(savedComment.getId())
                      .comments(savedComment.getComment())
                      .build();
          } else {
                throw new ResponseStatusException(UNAUTHORIZED, "Comment not found");

          }

        } catch (Exception e) {
            logger.error("Error editing comment: {}", e.getMessage());
            throw new RuntimeException("Failed to edit comment", e);

        }
    }
    @Transactional
    public List<GetCommentContainingName> getCommentContainingName (String name) {
        try {
            List<User> usersContainedName = userRepository.findByNameContaining(name);
            return  usersContainedName.stream()
                    .map(user -> {
                        Client client = clientRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Client not found"));
                        UserResponseDto userResponseDto = UserResponseDto.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .email(user.getUsername())
                                .role(user.getRole())
                                .build();
                        return GetCommentContainingName.builder()
                                .client(client)
                                .user(userResponseDto)
                                .build();
                    })
                    .collect(Collectors.toList()) ;

        } catch (Exception e) {
            logger.error("Error getting comment by name: {}", e.getMessage());
            throw new RuntimeException("Failed to get comment by name", e);
        }
    }
    @Transactional
    public DeleteCommentResponse deleteCommentById(Integer id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                throw new RuntimeException("User not authenticated");
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Comments comment = commentsRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Comment not found"));

            if ("ADMIN".equals(user.getRole())) {
                commentsRepository.delete(comment);
                commentsRepository.flush();
                return DeleteCommentResponse.builder()
                        .id(comment.getId())
                        .commentDeleted(comment.getComment())
                        .build();
            } else if ("CLIENT".equals(user.getRole())) {
                logger.info("user role is " + user.getRole());
                Client client = clientRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new RuntimeException("Client not found"));
                logger.info("client.getComments() " + client.getComments());
                if (!client.getComments().contains(comment)) {
                    throw new RuntimeException("You are not allowed to delete this comment");
                } else {
                    boolean contained = client.getComments().contains(comment);
                    logger.info("comment is contained " + contained);
                    logger.info("comment will be deleted " + comment);
                    client.getComments().remove(comment);
                    clientRepository.save(client);

                    commentsRepository.delete(comment);
                    commentsRepository.flush();
                    logger.info("Client deleted comment with id: {}", comment.getId());
                    return DeleteCommentResponse.builder()
                            .id(comment.getId())
                            .commentDeleted(comment.getComment())
                            .build();
                }
            } else {
                throw new RuntimeException("User not authorized because this role is " + user.getRole());
            }
        } catch (Exception e) {
            logger.error("Error deleting comment: {}", e.getMessage());
            throw new RuntimeException("Failed to delete comment", e);
        }
    }

}

