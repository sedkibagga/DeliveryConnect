package com.FB_APP.demo.products;

import com.FB_APP.demo.products.dtos.AddQuantityDto;
import com.FB_APP.demo.products.dtos.CreateProductDto;
import com.FB_APP.demo.products.dtos.ReduceQuantityDto;
import com.FB_APP.demo.products.dtos.UpdateProductByIdDto;
import com.FB_APP.demo.products.responses.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductsServices productsServices;
    private static final Logger logger = LoggerFactory.getLogger(ProductsController.class);
    @PreAuthorize("hasAnyAuthority('ADMIN','WAREHOUSE')")
    @PostMapping("/createProduct")
    public CreateProductResponse createProduct(@RequestBody CreateProductDto createProductDto) {
        return productsServices.createProduct(createProductDto);
    }
   @PreAuthorize("hasAnyAuthority('ADMIN','WAREHOUSE' , 'SEDENTARY')")
    @GetMapping("/getAllProducts")
    public List<GetAllProductsResponse> getAllProducts() {
        return productsServices.getAllProducts();
    }
   @PreAuthorize("hasAnyAuthority('ADMIN','WAREHOUSE' , 'SEDENTARY')")
    @GetMapping("/getProductContainingName/{productName}")
    public List<GetProductContainingNameResponse> getProductContainingName(@PathVariable String productName) {
        return productsServices.getProductContainingName(productName);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','WAREHOUSE')")
    @DeleteMapping("/deleteProductById/{id}")
    public DeleteProductByIdResponse deleteProductById(@PathVariable Integer id) {
        return productsServices.deleteProductById(id);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','WAREHOUSE')")
    @PatchMapping("/updateProductById/{id}")
    public UpdateProductByIdResponse updateProductById (@PathVariable Integer id , @RequestBody UpdateProductByIdDto updateProductByIdDto) {
        return productsServices.updateProductById(id,updateProductByIdDto);
    }

    @GetMapping("/productAvailability/{id}")
    public boolean productAvailability(@PathVariable Integer id) {
        return productsServices.productAvailability(id);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','WAREHOUSE' , 'SEDENTARY')")
    @GetMapping("/getProductsQuantities/{productName}")
    public List<GetProductsQuantitiesResponse> getProductsQuantities(@PathVariable String productName) {
        return productsServices.getProductsQuantities(productName);
    }

    @GetMapping("/getProductsAvailable")
    public List<GetProductsAvailableResponse> getProductsAvailable() {
        return productsServices.getProductsAvailable();
    }

    @PostMapping("/addQuantity/{id}")
    public AddQuantityResponse addQuantity(@PathVariable Integer id , @RequestBody AddQuantityDto quantityAdded) {
        logger.info("Adding quantity: {}", quantityAdded);
        return productsServices.addQuantity(id,quantityAdded);
    }

    @PostMapping("/reduceQuantity/{id}")
    public ReduceQuantityResponse reduceQuantity(@PathVariable Integer id , @RequestBody ReduceQuantityDto quantityReduced) {
        logger.info("Reducing quantity: {}", quantityReduced);
        return productsServices.reduceQuantity(id,quantityReduced);
    }
}
