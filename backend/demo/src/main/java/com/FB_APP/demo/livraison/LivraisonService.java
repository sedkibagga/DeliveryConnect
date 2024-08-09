package com.FB_APP.demo.livraison;
import com.FB_APP.demo.entities.Livraison;
import com.FB_APP.demo.entities.User;
import com.FB_APP.demo.livraison.dtos.CreateLivraisonDto;
import com.FB_APP.demo.livraison.dtos.UpdateLivraisonDto;
import com.FB_APP.demo.livraison.responses.*;
import com.FB_APP.demo.repositories.LivraisonRepository;
import com.FB_APP.demo.repositories.UserRepository;
import com.FB_APP.demo.user.UsersService;
import com.FB_APP.demo.user.dtos.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LivraisonService {
    private final LivraisonRepository livraisonRepository ;
    private final UserRepository userRepository ;
    private final static Logger logger = LoggerFactory.getLogger(LivraisonService.class);
    private final UsersService usersService;

    @Transactional
    public CreateLivraisonResponse createLivraison(CreateLivraisonDto createLivraisonDto) {
        try {
            var user = userRepository.findByName(createLivraisonDto.getUser_name())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            if (user.getRole() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User role is null");
            }
            logger.info("user: {}", user);

            logger.info("role: {}", user.getRole());
            if(!usersService.isValidRole(user.getRole())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role: " + user.getRole());
            }
            if (!user.getRole().equals("DELIVERY")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a DELIVERY");
            }
            var livraison = Livraison.builder()
                    .localisation(createLivraisonDto.getLocalisation())
                    .time(createLivraisonDto.getTime())
                    .num_tel(createLivraisonDto.getNum_tel())
                    .date(createLivraisonDto.getDate())
                    .product_name(createLivraisonDto.getProduct_name())
                    .user(user)
                    .status("En cours")
                    .build();
            livraisonRepository.save(livraison) ;
            var userResponseDto = UserResponseDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
            return CreateLivraisonResponse.builder()
                    .localisation(livraison.getLocalisation())
                    .time(livraison.getTime())
                    .num_tel(livraison.getNum_tel())
                    .DELIVERY(userResponseDto)
                    .date(livraison.getDate())
                    .product_name(livraison.getProduct_name())
                    .status(livraison.getStatus())
                    .build();


        } catch (Exception e) {
           throw new RuntimeException("failed to save Livraison" + e.getMessage());
        }
    }

    public List<GetAllLivraisonResponse> getAllLivraisons () {
        try {
            var livraisons = livraisonRepository.findAll();
            logger.info("livraisons: {}", livraisons);

            return livraisons.stream()
                    .map(livraison -> {
                        var user = livraison.getUser();
                        var userResponseDto = UserResponseDto.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .build();
                        return GetAllLivraisonResponse.builder()
                                .id(livraison.getId())
                                .localisation(livraison.getLocalisation())
                                .num_tel(livraison.getNum_tel())
                                .time(livraison.getTime())
                                .DELIVERY(userResponseDto)
                                .status(livraison.getStatus())
                                .product_name(livraison.getProduct_name())
                                .date(livraison.getDate())
                                .build();
                    })
                    .collect(Collectors.toList()) ;



        } catch (Exception e) {
            throw new RuntimeException("failed to get Livraisons" + e.getMessage());
        }
    }

    @Transactional
    public UpdateLivraisonResponse updateLivraison (Integer id , UpdateLivraisonDto updateLivraisonDto) {
        try {
            var livraison = livraisonRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livraison not found"));
            updateLivraisonDto.getLocalisation().ifPresent(livraison::setLocalisation);
            updateLivraisonDto.getTime().ifPresent(livraison::setTime);
            updateLivraisonDto.getNum_tel().ifPresent(livraison::setNum_tel);
            updateLivraisonDto.getDate().ifPresent(livraison::setDate);
            updateLivraisonDto.getProduct_name().ifPresent(livraison::setProduct_name);

            livraisonRepository.save(livraison);
            return UpdateLivraisonResponse.builder()
                    .id(id)
                    .localisation(livraison.getLocalisation())
                    .time(livraison.getTime())
                    .num_tel(livraison.getNum_tel())
                    .date(livraison.getDate())
                    .product_name(livraison.getProduct_name())
                    .status(livraison.getStatus())
                    .build();
        } catch (Exception e) {
            logger.error("Error updating Livraison: {}", e.getMessage());
            throw new RuntimeException("failed to update Livraison" + e.getMessage());
        }
    }

    @Transactional
    public DeleteLivraisonResponse deleteLivraison (Integer id) {
        try {
            var livraison = livraisonRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livraison not found"));
            livraisonRepository.delete(livraison);
            return DeleteLivraisonResponse.builder()
                    .id(id)
                    .localisation(livraison.getLocalisation())
                    .time(livraison.getTime())
                    .num_tel(livraison.getNum_tel())
                    .date(livraison.getDate())
                    .product_name(livraison.getProduct_name())
                    .status(livraison.getStatus())
                    .build();

    } catch (Exception e) {
        logger.error("Error deleting Livraison: {}", e.getMessage());
        throw new RuntimeException("failed to delete Livraison" + e.getMessage());
    }

    }

    @Transactional
    public List<GetLivraisonByDeliveryNameResponse> getLivraisonByDeliveryName(String name) {
        try {

            User user = userRepository.findByName(name)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


            List<Livraison> livraisons = livraisonRepository.findByUser(user);

            if (livraisons.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Livraisons not found");
            }


            return livraisons.stream()
                    .map(livraison -> {
                        var userDto = UserResponseDto.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .build();
                        return GetLivraisonByDeliveryNameResponse.builder()
                                .id(livraison.getId())
                                .localisation(livraison.getLocalisation())
                                .num_tel(livraison.getNum_tel())
                                .time(livraison.getTime())
                                .DELIVERY(userDto)
                                .product_name(livraison.getProduct_name())
                                .date(livraison.getDate())
                                .status(livraison.getStatus())
                                .build();
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error retrieving Livraisons: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get Livraisons: " + e.getMessage());
        }
    }




}
