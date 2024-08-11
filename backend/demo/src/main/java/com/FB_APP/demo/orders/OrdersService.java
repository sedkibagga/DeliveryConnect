package com.FB_APP.demo.orders;

import com.FB_APP.demo.entities.*;
import com.FB_APP.demo.orders.dtos.AddOrderDto;
import com.FB_APP.demo.orders.dtos.ClientOrderNameDto;
import com.FB_APP.demo.orders.dtos.EditOrderDto;
import com.FB_APP.demo.orders.responses.*;
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

    public GetOrderByIdResponse getOrderById(Integer id) {
        try {
            Orders order = ordersRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found"));
            return GetOrderByIdResponse.builder()
                    .id(order.getId())
                    .location(order.getLocation())
                    .date(order.getDate())
                    .time(order.getTime())
                    .products(order.getProducts())
                    .build();
        } catch (Exception e) {

            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    public List< GetOrderContainingClientNameResponse> getOrderContainingClientName(String clientName) {
        try {
            logger.info("clientName: {}", clientName);
           List<User> users = userRepository.findByNameContaining(clientName);
           logger.info("users: {}", users);
          return users.stream()
                  .filter(user -> "CLIENT".equals(user.getRole()))
                   .map(user -> {
                       Client client = clientRepository.findByUserId(user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client not found"));
                       List<Orders> orders = ordersRepository.findByClient(client);
                       return GetOrderContainingClientNameResponse.builder()
                               .clientName(user.getName())
                               .orders(orders)
                               .build();
                   })
                   .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
   public List<GetAllOrdersResponse>getAllOrders() {
        try {
            List<Orders> orders = ordersRepository.findAll();
            return  orders.stream()
                    .map(order -> {
                        return GetAllOrdersResponse.builder()
                                .orders(order)
                                .build();
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
   }

   public List<GetOrdersOfClientResponse> getOrdersOfClient() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication==null||!(authentication.getPrincipal() instanceof UserDetails)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
            if (!("CLIENT".equals(user.getRole()))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized and is not a client");
            }
            Client client = clientRepository.findByUserId(user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client not found"));
            List<Orders> orders = client.getOrders();
            return orders.stream()
                    .map(order -> {
                        return GetOrdersOfClientResponse.builder()
                                .order(order)
                                .build();
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
   }

    public EditOrderResponse editOrder(Integer id, EditOrderDto editOrderDto, Integer productId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

            if (!"CLIENT".equals(user.getRole())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized and is not a client");
            }

            Orders order = ordersRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found"));

            if (!order.getClient().getUserId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized and is not the client of the order");
            }

            editOrderDto.getLocation().ifPresent(order::setLocation);
            editOrderDto.getDate().ifPresent(order::setDate);
            editOrderDto.getTime().ifPresent(order::setTime);
          Orders savedOrder = ordersRepository.save(order);
         logger.info("savedOrder: {}", savedOrder);
           if (editOrderDto.getNewProductId().isPresent()) {
               Optional<Products> products = productsRepository.findById(editOrderDto.getNewProductId().get());

               if (products.isPresent()) {
                   logger.info("products: {}", products.get());
                   List<OrderProducts> productsEdited = orderProductsRepository.findAllByProductId(productId);
                   logger.info("productsEdited: {}", productsEdited);
                   for (OrderProducts productEdited : productsEdited) {
                       productEdited.setProductId(products.get().getId());
                       logger.info("Old productEdited.setProductPrice(products.get().getProductPrice()): {}", productEdited.getProductPrice());
                       productEdited.setProductPrice(products.get().getProductPrice());
                       logger.info("NewproductEdited.setProductPrice(products.get().getProductPrice());: {}", productEdited.getProductPrice());
                       productEdited.setQuantity(products.get().getQuantity());
                       productEdited.setProductName(products.get().getProductName());
                       orderProductsRepository.save(productEdited);
                   }
               }
           }
            return EditOrderResponse.builder()
                    .client(order.getClient())
                    .build();

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
