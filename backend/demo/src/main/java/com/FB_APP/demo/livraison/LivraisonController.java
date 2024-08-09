package com.FB_APP.demo.livraison;

import com.FB_APP.demo.livraison.dtos.CreateLivraisonDto;
import com.FB_APP.demo.livraison.dtos.UpdateLivraisonDto;
import com.FB_APP.demo.livraison.responses.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livraison")
@RequiredArgsConstructor
public class LivraisonController {
    private final LivraisonService livraisonService;
   private final static Logger logger = LoggerFactory.getLogger(LivraisonController.class);

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SEDENTARY')")
    @PostMapping("/createLivraison")
    public ResponseEntity<CreateLivraisonResponse> createLivraison(@RequestBody CreateLivraisonDto createLivraisonDto) {
         logger.info("createLivraisonDto: {}", createLivraisonDto);
        CreateLivraisonResponse response = livraisonService.createLivraison(createLivraisonDto);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SEDENTARY' , 'CLIENT')")
    @GetMapping("/getAllLivraisons")
    public List<GetAllLivraisonResponse> getAllLivraisons () {
        return livraisonService.getAllLivraisons();
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SEDENTARY')")
    @PatchMapping("/updateLivraison/{id}")
    public ResponseEntity<UpdateLivraisonResponse> updateLivraison(@PathVariable Integer id, @RequestBody UpdateLivraisonDto updateLivraisonDto) {
        UpdateLivraisonResponse response = livraisonService.updateLivraison(id, updateLivraisonDto);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SEDENTARY')")
    @DeleteMapping("/deleteLivraison/{id}")
    public ResponseEntity<DeleteLivraisonResponse> deleteLivraison(@PathVariable Integer id) {
        DeleteLivraisonResponse response = livraisonService.deleteLivraison(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SEDENTARY')")
    @GetMapping("/getLivraisonByDeliveryName/{name}")
    public List<GetLivraisonByDeliveryNameResponse> getLivraisonByDeliveryName(@PathVariable String name) {

        return livraisonService.getLivraisonByDeliveryName(name);


    }
}
