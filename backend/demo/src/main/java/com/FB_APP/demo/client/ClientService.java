package com.FB_APP.demo.client;

import com.FB_APP.demo.client.dtos.CreateClientProductsDto;
import com.FB_APP.demo.client.dtos.CreateClitentDto;
import com.FB_APP.demo.client.responses.CreateClientProductsResponse;
import com.FB_APP.demo.client.responses.CreateClientResponse;
import com.FB_APP.demo.client.responses.GetAllClientsResponse;
import com.FB_APP.demo.client.responses.GetClientByNameResponse;
import com.FB_APP.demo.config.JwtService;
import com.FB_APP.demo.entities.Client;
import com.FB_APP.demo.entities.User;
import com.FB_APP.demo.repositories.ClientRepository;
import com.FB_APP.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class ClientService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    public CreateClientResponse createClient(CreateClitentDto createClitentDto) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(createClitentDto.getEmail());
            if (existingUser.isPresent()) {
                throw new ResponseStatusException(BAD_REQUEST, "User with email " + createClitentDto.getEmail() + " already exists");
            }

             String encodedPassword = passwordEncoder.encode(createClitentDto.getPassword());
            var user = User.builder()
                    .name(createClitentDto.getName())
                    .email(createClitentDto.getEmail())
                    .password(encodedPassword)
                    .role("CLIENT")
                    .build();

            var userSaved = userRepository.save(user);
            Integer user_id = userSaved.getId() ;
            var client = Client.builder()
                    .userId(user_id)
                    .build();
            clientRepository.save(client);
            String token = jwtService.generateToken(user);
            return new CreateClientResponse(token);

        } catch (Exception e) {
            logger.error("Error creating client: {}", e.getMessage());
            throw new RuntimeException("Failed to create client");
        }
    }

    public CreateClientProductsResponse createClientProducts(CreateClientProductsDto createClientProductsDto) {
        try {
            logger.info("Creating client products: {}", createClientProductsDto);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.info("Authentication: {}", authentication);
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                throw new ResponseStatusException(UNAUTHORIZED, "User not authenticated");
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.info("User details: {}", userDetails);
            var user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "User not found"));
            String role = user.getRole() ;
            logger.info("User role: {}", role);
            if (!role.equals("CLIENT")) {
                throw new ResponseStatusException(UNAUTHORIZED, "User not authorized and is not a client, role: " + role);
            }
            Optional<Client> existingClient = clientRepository.findByUserId(user.getId());
            if (existingClient.isPresent()) {
                Client client = existingClient.get();
                client.getProducts().addAll(createClientProductsDto.getProducts());
                clientRepository.save(client);
                return new CreateClientProductsResponse(client.getId(), client.getProducts());
            }
            else {
                throw new ResponseStatusException(BAD_REQUEST, "Client not found");
            }
        } catch (Exception e) {
            logger.error("Error creating client Products: {}", e.getMessage());
            throw new RuntimeException("Failed to create  client Products");
        }
    }

    public List<GetAllClientsResponse> getAllClients() {
        try {
            var clients = clientRepository.findAll();
            return clients.stream()

                    .map(client -> {
                        Integer clientId = client.getUserId() ;
                        var user = userRepository.findById(clientId).orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "User not found"));
                        String clientName = user.getName() ;
                        return GetAllClientsResponse.builder()
                                .id(client.getId())
                                .clientName(clientName)
                                .products(client.getProducts())
                                .comments(client.getComments())
                                .build();
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting all clients: {}", e.getMessage());
            throw new RuntimeException("Failed to get all clients");
        }
    }

    public GetClientByNameResponse getClientByName(String clientName) {
        try {
            var user = userRepository.findByName(clientName).orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "User not found"));
            Integer userClientId = user.getId() ;
            var client = clientRepository.findByUserId(userClientId)
                    .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Client not found"));
            return GetClientByNameResponse.builder()
                    .id (client.getId())
                    .clientName(clientName)
                    .products(client.getProducts())
                    .comments(client.getComments())
                    .build();
        } catch (Exception e) {
            logger.error("Error getting client by name: {}", e.getMessage());
            throw new RuntimeException("Failed to get client by name");
        }
    }
}
