package com.FB_APP.demo.orders;

import com.FB_APP.demo.entities.*;
import com.FB_APP.demo.orders.dtos.AddOrderDto;
import com.FB_APP.demo.orders.dtos.ClientOrderNameDto;
import com.FB_APP.demo.repositories.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProductsRepository productsRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrdersService.class);

    public AddOrderResponse addOrder (AddOrderDto addOrderDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication==null||!(authentication.getPrincipal() instanceof UserDetails)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
            if(!("CLIENT".equals(user.getRole()))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized because this role is " + user.getRole());
            }
            Client client = clientRepository.findByUserId(user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client not found"));
            Orders order = Orders.builder()
                    .date(addOrderDto.getDate())
                    .time(addOrderDto.getTime())
                    .location(addOrderDto.getLocation())
                    .client(client)
                    .build();
            Orders savedOrder = ordersRepository.save(order);
            List<OrderProducts> orderProductsList = addOrderDto.getProducts().stream()
                    .map(product -> {
                        Optional<Products> optionalProducts = productsRepository.findById(product.getId());
                        if (optionalProducts.isPresent()) {
                            Products products = optionalProducts.get();
                            return OrderProducts.builder()
                                    .productId(products.getId())
                                    .productName(products.getProductName())
                                    .productPrice(products.getProductPrice())
                                    .quantity(product.getQuantity())
                                    .orders(savedOrder)
                                    .build();
                        } else {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");

                        }
                    })
                    .collect(Collectors.toList());
            List<OrderProducts> savedOrderProducts = orderProductsRepository.saveAll(orderProductsList);
            savedOrder.setProducts(savedOrderProducts);
            ordersRepository.save(savedOrder);
            ClientOrderNameDto clientOrderNameDto = ClientOrderNameDto.builder()
                    .id(client.getId())
                    .userId(client.getUserId())
                    .clientName(user.getName())
                    .comments(client.getComments())
                    .claims(client.getClaims())
                    .orders(client.getOrders())
                    .build();
        return AddOrderResponse.builder()
                .id(savedOrder.getId())
                .client(clientOrderNameDto)
                .build();

        } catch (Exception e) {

            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
