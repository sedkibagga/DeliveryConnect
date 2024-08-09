package com.FB_APP.demo.claims.dtos;

import com.FB_APP.demo.entities.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClaimdto {
    private Optional<String> claim = Optional.empty();
}
