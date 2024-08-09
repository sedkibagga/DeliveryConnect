package com.FB_APP.demo.user;
import com.FB_APP.demo.user.dtos.CreateUserDto;
import com.FB_APP.demo.user.dtos.LoginUserDto;
import com.FB_APP.demo.user.responses.CreateUserResponse;
import com.FB_APP.demo.user.responses.GetAllUsersResponse;
import com.FB_APP.demo.user.responses.LoginUserResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UsersController {
   private final UsersService usersService;
   private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/auth/signup")
    public ResponseEntity<CreateUserResponse> createUser (@RequestBody CreateUserDto createUserDto) {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      logger.info("User logged in: {}", authentication.getName());
       logger.info("User authorities: {}", authentication.getAuthorities());
       return ResponseEntity.ok(usersService.createUser(createUserDto)) ;
   }

   @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> loginUser(@RequestBody LoginUserDto loginUserDto) {
       return ResponseEntity.ok(usersService.loginUser(loginUserDto)) ;
   }

   @GetMapping("/user/getAllUsers")
   public List<GetAllUsersResponse> getAllUsers() {
       return usersService.getAllUsers();
   }

}
