package com.FB_APP.demo.claims;

import com.FB_APP.demo.claims.dtos.CreateClaimDto;
import com.FB_APP.demo.claims.dtos.UpdateClaimdto;
import com.FB_APP.demo.claims.responses.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/claims")
@RequiredArgsConstructor
public class ClaimsController {
    private final ClaimsService claimsService ;
    private static final Logger logger = LoggerFactory.getLogger(ClaimsController.class);
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    @PostMapping("/createClaim")
    public CreateClaimResponse createClaim(@RequestBody CreateClaimDto createClaimDto) {
        return claimsService.createClaim(createClaimDto);
    }

    @GetMapping("/getAllClaims")
    public List<GetAllClaimsResponse> getAllClaims() {
        return claimsService.getAllClaims();
    }

    @GetMapping("/getClaimContainingName/{name}")
    public List<GetClaimContainingName> getClaimContainingName(@PathVariable String name) {
        return claimsService.getClaimContainingNames(name);
    }
    @PreAuthorize("hasAnyAuthority( 'CLIENT')")
    @PatchMapping("/updateClaim/{id}")
    public UpdateClaimResponse updateClaim(@PathVariable Integer id, @RequestBody UpdateClaimdto updateClaimdto) {
        return claimsService.updateClaim(id,updateClaimdto);
    }
     @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    @DeleteMapping("/deleteClaim/{id}")
    public DeleteClaimResponse deleteClaim(@PathVariable Integer id) {
      return   claimsService.deleteClaim(id);
    }


}
