package com.FB_APP.demo.products;

import com.FB_APP.demo.entities.Products;
import com.FB_APP.demo.entities.User;
import com.FB_APP.demo.products.dtos.AddQuantityDto;
import com.FB_APP.demo.products.dtos.CreateProductDto;
import com.FB_APP.demo.products.dtos.ReduceQuantityDto;
import com.FB_APP.demo.products.dtos.UpdateProductByIdDto;
import com.FB_APP.demo.products.responses.*;
import com.FB_APP.demo.repositories.ProductsRepository;
import com.FB_APP.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsServices {
    private final ProductsRepository productsRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductsServices.class);
    public CreateProductResponse createProduct(CreateProductDto createProductDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication==null || !(authentication.getPrincipal() instanceof UserDetails)) {
                throw new RuntimeException("User not found");
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
            if (!(createProductDto.getQuantity() >= 0)) {
                throw new RuntimeException("Quantity cannot be less than zero");
            }
            if ("ADMIN".equals(user.getRole())) {
                logger.info("Creating product: {}", createProductDto);
                Products products = Products.builder()
                        .productName(createProductDto.getProductName())
                        .productPrice(createProductDto.getProductPrice())
                        .quantity(createProductDto.getQuantity())
                        .build();
                Products savedProduct = productsRepository.save(products);
                logger.info("Product created successfully: " + savedProduct);
                return CreateProductResponse.builder()
                        .id(savedProduct.getId())
                        .ProductName(savedProduct.getProductName())
                        .ProductPrice(savedProduct.getProductPrice())
                        .quantity(savedProduct.getQuantity())
                        .build();
            }
            else if ("WAREHOUSE".equals(user.getRole())) {
                Products products = Products.builder()
                        .productName(createProductDto.getProductName())
                        .productPrice(createProductDto.getProductPrice())
                        .quantity(createProductDto.getQuantity())
                        .build();
                Products savedProduct = productsRepository.save(products);
                return CreateProductResponse.builder()
                        .id(savedProduct.getId())
                        .ProductName(savedProduct.getProductName())
                        .ProductPrice(savedProduct.getProductPrice())
                        .quantity(savedProduct.getQuantity())
                        .build();
            } else {
                throw new RuntimeException("User not authorized to create product because it is not an admin or warehouse");
            }
        } catch (Exception e) {
            logger.error("Error while creating product: " + e.getMessage());
            throw new RuntimeException("Error while creating product: " + e.getMessage());
        }
    }

    public List<GetAllProductsResponse> getAllProducts() {
        try {
            var products = productsRepository.findAll();
            return products.stream()
                    .map(product -> {
                        return GetAllProductsResponse.builder()
                                .id(product.getId())
                                .productName(product.getProductName())
                                .productPrice(product.getProductPrice())
                                .quantity(product.getQuantity())
                                .build();
                    })
                    .collect(Collectors.toList()) ;

        } catch (Exception e) {
            logger.error("Error getting all products: {}", e.getMessage());
            throw new RuntimeException("Failed to get all products");
        }
    }

    public List<GetProductContainingNameResponse> getProductContainingName(String productName) {
        try {
           var products = productsRepository.findByProductNameContaining(productName);

               return  products.stream()
                       .map(product -> {
                           return GetProductContainingNameResponse.builder()
                                   .id(product.getId())
                                   .productName(product.getProductName())
                                   .productPrice(product.getProductPrice())
                                   .quantity(product.getQuantity())
                                   .build();
                       })
                       .collect(Collectors.toList()) ;

        } catch (Exception e) {
            logger.error("Error getting  productContainingName: {}", e.getMessage());
            throw new RuntimeException("Failed to get productContainingName");
        }
    }
    public DeleteProductByIdResponse deleteProductById (Integer id ) {
        try {
            Products product = productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            productsRepository.deleteById(id);
            return DeleteProductByIdResponse.builder()
                    .id(product.getId())
                    .productNameDeleted(product.getProductName())
                    .productPrice(product.getProductPrice())
                    .quantity(product.getQuantity())
                    .build();
        } catch (Exception e) {
            logger.error("Error deleting productById: {}", e.getMessage());
            throw new RuntimeException("Failed to delete productById");
        }
    }

    public UpdateProductByIdResponse updateProductById(Integer id , UpdateProductByIdDto updateProductByIdDto) {
        try {
            Products product = productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            if (updateProductByIdDto.getProductName().isPresent()) {
                product.setProductName(updateProductByIdDto.getProductName().get());
            }

            if (updateProductByIdDto.getProductPrice().isPresent()) {
                product.setProductPrice(updateProductByIdDto.getProductPrice().get());
            }
            if (updateProductByIdDto.getQuantity().isPresent() && updateProductByIdDto.getQuantity().get() >= 0) {
                product.setQuantity(updateProductByIdDto.getQuantity().get());
            }

            Products savedProduct = productsRepository.save(product);
            return UpdateProductByIdResponse.builder()
                    .id(savedProduct.getId())
                    .productNameUpdated(savedProduct.getProductName())
                    .productPrice(savedProduct.getProductPrice())
                    .quantity(savedProduct.getQuantity())
                    .build();
        } catch (Exception e) {
            logger.error("Error updating productById: {}", e.getMessage());
            throw new RuntimeException("Failed to update productById");
        }
    }

    public boolean productAvailability(Integer id) {
        try {
             Products product = productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            return product.getQuantity() > 0;
        } catch (Exception e) {
            logger.error("Error finding productById: {}", e.getMessage());
            throw new RuntimeException("Failed to find productById");
        }
    }

    public List<GetProductsQuantitiesResponse> getProductsQuantities(String productName) {
        try {
             List<Products> products = productsRepository.findByProductNameContaining(productName);
             return products.stream()
                     .map(product -> {
                         return GetProductsQuantitiesResponse.builder()
                                 .productName(product.getProductName())
                                 .quantity(product.getQuantity())
                                 .build();
                     })
                     .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting productsQuantities: {}", e.getMessage());
            throw new RuntimeException("Failed to get productsQuantities");
        }
    }
    public List<GetProductsAvailableResponse> getProductsAvailable() {
        try {
            List<Products> products = productsRepository.findAll();

            return products.stream()
                    .filter(product -> productAvailability(product.getId()))
                    .map(product -> {
                        return GetProductsAvailableResponse.builder()
                                .id(product.getId())
                                .productName(product.getProductName())
                                .productPrice(product.getProductPrice())
                                .quantity(product.getQuantity())
                                .build();
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error getting productsAvailable: {}", e.getMessage());
            throw new RuntimeException("Failed to get productsAvailable");
        }
    }

    public AddQuantityResponse addQuantity(Integer id , AddQuantityDto quantityAdded) {
        try {
            logger.info("Adding quantity: {}", quantityAdded);
            Products product = productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            if (!(quantityAdded.getQuantityAdded() >= 0)) {
                throw new RuntimeException("Quantity must be greater than 0");
            }
            Integer newQuantity = product.getQuantity()+ quantityAdded.getQuantityAdded();
            product.setQuantity(newQuantity);
          Products savedProduct = productsRepository.save(product);
            return AddQuantityResponse.builder()
                    .productName(savedProduct.getProductName())
                    .quantity(savedProduct.getQuantity())
                    .build();

        } catch (Exception e) {
            logger.error("Error adding quantity: {}", e.getMessage());
            throw new RuntimeException("Failed to add quantity");
        }
    }

    public ReduceQuantityResponse reduceQuantity(Integer id , ReduceQuantityDto quantityReduced) {
        try {
            Products product = productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            if(!(quantityReduced.getQuantityReduced() >= 0)) {
                throw new RuntimeException("Quantity must be greater than 0");
            }
            else if (!(quantityReduced.getQuantityReduced() <= product.getQuantity())) {
                throw new RuntimeException("Quantity must be less than or equal to the current quantity");
            }

            Integer newQuantity = product.getQuantity()- quantityReduced.getQuantityReduced();
            product.setQuantity(newQuantity);
            Products savedProduct = productsRepository.save(product);
            return ReduceQuantityResponse.builder()
                    .productName(savedProduct.getProductName())
                    .quantity(savedProduct.getQuantity())
                    .build();
        }catch (Exception e) {
            logger.error("Error reducing quantity: {}", e.getMessage());
            throw new RuntimeException("Failed to reduce quantity");
        }
    }

}
