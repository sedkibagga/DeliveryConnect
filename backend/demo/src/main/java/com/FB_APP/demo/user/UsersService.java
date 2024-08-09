package com.FB_APP.demo.user;

import com.FB_APP.demo.config.JwtService;
import com.FB_APP.demo.entities.User;
import com.FB_APP.demo.enums.Roles;
import com.FB_APP.demo.repositories.UserRepository;
import com.FB_APP.demo.user.dtos.CreateUserDto;
import com.FB_APP.demo.user.dtos.LoginUserDto;
import com.FB_APP.demo.user.responses.CreateUserResponse;
import com.FB_APP.demo.user.responses.GetAllUsersResponse;
import com.FB_APP.demo.user.responses.LoginUserResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class UsersService {
    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public CreateUserResponse createUser(CreateUserDto createUserDto) {
        if (!isValidRole(createUserDto.getRole())) {
            throw new ResponseStatusException(BAD_REQUEST, "Invalid role: " + createUserDto.getRole());
        }

        Optional<User> existingUser = userRepository.findByEmail(createUserDto.getEmail());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(BAD_REQUEST, "User with email " + createUserDto.getEmail() + " already exists");
        }

        String encodedPassword = passwordEncoder.encode(createUserDto.getPassword());
        var user = User.builder()
                .name(createUserDto.getName())
                .email(createUserDto.getEmail())
                .password(encodedPassword)
                .role(createUserDto.getRole())
                .build();

        try {
            userRepository.save(user);
            String token = jwtService.generateToken(user);
            return new CreateUserResponse(token);
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
            throw new RuntimeException("Failed to create user");
        }
    }

    public boolean isValidRole(String role) {
        if (role == null) {
            logger.warn("Role is null");
            return false;
        }
        return Roles.ALL_ROLES.contains(role);
    }

    public LoginUserResponse loginUser(LoginUserDto loginUserDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUserDto.getEmail(),
                            loginUserDto.getPassword()
                    )
            );

            var user = userRepository.findByEmail(loginUserDto.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "User not found"));

            var token = jwtService.generateToken(user);
            logger.info("Generated JWT for user with email: {}", loginUserDto.getEmail());

            return LoginUserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .token(token)
                    .build();
        } catch (Exception e) {
            logger.error("Error logging in user: {}", e.getMessage());
            throw new ResponseStatusException(UNAUTHORIZED, "Failed to login");
        }
    }

    public List<GetAllUsersResponse> getAllUsers() {
        try {
            var users = userRepository.findAll();
            return users.stream()
                    .map(user -> {
                        return GetAllUsersResponse.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .livraisons(user.getLivraisons())
                                .build();
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error getting all users: {}", e.getMessage());
            throw new RuntimeException("Failed to get all users");
        }
    }
}
