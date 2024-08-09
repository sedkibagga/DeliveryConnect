package com.FB_APP.demo.client;

import com.FB_APP.demo.client.dtos.CreateClientProductsDto;
import com.FB_APP.demo.client.dtos.CreateClitentDto;
import com.FB_APP.demo.client.responses.CreateClientProductsResponse;
import com.FB_APP.demo.client.responses.CreateClientResponse;
import com.FB_APP.demo.client.responses.GetAllClientsResponse;
import com.FB_APP.demo.client.responses.GetClientByNameResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @PostMapping("/createClient")
    public CreateClientResponse createClient(@RequestBody CreateClitentDto createClitentDto) {
        return clientService.createClient(createClitentDto);
    }
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    @PostMapping("/client/createClientProducts")
    public CreateClientProductsResponse createClientProducts(@RequestBody CreateClientProductsDto createClientProductsDto) {
        logger.info("Creating client products: {}", createClientProductsDto);
        return clientService.createClientProducts(createClientProductsDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SEDENTARY')")
    @GetMapping("/client/getAllClients")
    public List<GetAllClientsResponse> getAllClients() {
        return clientService.getAllClients();
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/client/getClientByName/{clientName}")
    public GetClientByNameResponse getClientByName(@PathVariable String clientName) {
        return clientService.getClientByName(clientName);
    }
}
