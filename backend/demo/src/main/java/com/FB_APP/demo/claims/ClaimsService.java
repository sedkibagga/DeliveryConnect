package com.FB_APP.demo.claims;

import com.FB_APP.demo.claims.dtos.ClientDto;
import com.FB_APP.demo.claims.dtos.CreateClaimDto;
import com.FB_APP.demo.claims.dtos.UpdateClaimdto;
import com.FB_APP.demo.claims.responses.*;
import com.FB_APP.demo.entities.Claims;
import com.FB_APP.demo.entities.Client;
import com.FB_APP.demo.entities.User;
import com.FB_APP.demo.repositories.ClaimsRepository;
import com.FB_APP.demo.repositories.ClientRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaimsService {
    private final ClaimsRepository claimsRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
   private static final Logger logger = LoggerFactory.getLogger(ClaimsService.class);

    @Transactional
    public CreateClaimResponse createClaim(CreateClaimDto createClaimDto){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                throw new RuntimeException("User not authenticated");
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
            if (!"CLIENT".equals(user.getRole())) {
                throw new RuntimeException("User not authorized and is not a client");
            }
            Client client = clientRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Client not found"));
            UserResponseDto userResponseDto = UserResponseDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
            Claims claims = Claims.builder()
                    .claim(createClaimDto.getClaim())
                    .client(client)
                    .build();
           Claims savedClaim =  claimsRepository.save(claims);
            return CreateClaimResponse.builder()
                    .id(savedClaim.getId())
                    .claim(savedClaim.getClaim())
                    .client(savedClaim.getClient())
                    .user(userResponseDto)
                    .build();
        } catch (Exception e) {
            logger.error("Error creating Claim: {}", e.getMessage());
            throw new RuntimeException("failed to create Claim" + e.getMessage());
        }
    }
    @Transactional
    public List<GetAllClaimsResponse> getAllClaims() {
        try {
            List<Claims> allClaims = claimsRepository.findAll();
            return allClaims.stream()
                    .map(claims -> {
                   User user = userRepository.findById(claims.getClient().getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
                   UserResponseDto userResponseDto = UserResponseDto.builder()
                           .id(user.getId())
                           .name(user.getName())
                           .email(user.getEmail())
                           .role(user.getRole())
                           .build();
                   return GetAllClaimsResponse.builder()
                           .id(claims.getId())
                           .claim(claims.getClaim())
                           .client(claims.getClient())
                           .user(userResponseDto)
                           .build();
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting all claims: {}", e.getMessage());
            throw new RuntimeException("Failed to get all claims", e);
        }
    }
    @Transactional
    public List<GetClaimContainingName> getClaimContainingNames(String name) {
        try {
            List<User> usersContainedName = userRepository.findByNameContaining(name);
            return usersContainedName.stream()
                    .map(user -> {
                        Client client = clientRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Client not found"));
                        UserResponseDto userResponseDto = UserResponseDto.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .email(user.getUsername())
                                .role(user.getRole())
                                .build();
                        return GetClaimContainingName.builder()
                                .client(client)
                                .user(userResponseDto)
                                .build();
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting claim by name: {}", e.getMessage());
            throw new RuntimeException("Failed to get claim by name", e);
        }
    }
    @Transactional
    public UpdateClaimResponse updateClaim(Integer id ,UpdateClaimdto updateClaimdto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication==null||!(authentication.getPrincipal() instanceof UserDetails)) {
                throw new RuntimeException("User not authenticated");
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
            if (!"CLIENT".equals(user.getRole())) {
                throw new RuntimeException("User not authorized and is not a client" + user.getRole());
            }

            Client client = clientRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Client not found"));
            Claims claim = claimsRepository.findById(id).orElseThrow(() -> new RuntimeException("Claim not found"));
            if (!client.getClaims().contains(claim)) {
                throw new RuntimeException("User not authorized to update claim");
            }

            String newClaimText = updateClaimdto.getClaim().orElse(claim.getClaim());
            claim.setClaim(newClaimText);
            claimsRepository.save(claim);

            ClientDto clientDto = ClientDto.builder()
                    .id(client.getId())
                    .userId(client.getUserId())
                    .claims(client.getClaims())
                    .build();

            UserResponseDto userResponseDto = UserResponseDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();

            return UpdateClaimResponse.builder()
                    .client(clientDto)
                    .user(userResponseDto)
                    .build();

        } catch (Exception e) {
            logger.error("Error updating claim: {}", e.getMessage());
            throw new RuntimeException("Failed to update claim", e);
        }
    }
    @Transactional
    public DeleteClaimResponse deleteClaim(Integer id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication==null||!(authentication.getPrincipal() instanceof UserDetails)) {
                throw new RuntimeException("User not authenticated");
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
            if ("ADMIN".equals(user.getRole())) {
                Claims claim = claimsRepository.findById(id).orElseThrow(() -> new RuntimeException("Claim not found"));
                 claimsRepository.delete(claim);
                 return DeleteClaimResponse.builder()
                         .id(claim.getId())
                         .claim(claim.getClaim())
                         .build();
            } else if ("CLIENT".equals(user.getRole())) {
                Client client = clientRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Client not found"));
                Claims claim = claimsRepository.findById(id).orElseThrow(() -> new RuntimeException("Claim not found"));
                if (!client.getClaims().contains(claim)) {
                    throw new RuntimeException("User not authorized to delete claim because it is not your claim" );
                }

                client.getClaims().remove(claim) ;
                clientRepository.save(client);
                claimsRepository.delete(claim);
                return DeleteClaimResponse.builder()
                        .id(claim.getId())
                        .claim(claim.getClaim())
                        .build();
            }
            else {
                throw new RuntimeException("User not authorized to delete claim");
            }
        } catch (Exception e) {
            logger.error("Error deleting claim: {}", e.getMessage());
            throw new RuntimeException("Failed to delete claim", e);
        }
    }


}
