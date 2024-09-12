package com.product_service.controller;

import com.product_service.dto.*;
import com.product_service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ResponseProduct> createProduct(@Valid @RequestBody RequestProduct requestProduct) {
        ResponseProduct responseProduct = productService.createProduct(requestProduct);
        return ResponseEntity.ok(responseProduct);
    }

    @PostMapping("/getall")

    public ResponseEntity<ResponseAllProduct> getAllProducts() {
        ResponseAllProduct responseAllProduct = productService.getAllProducts();
        return ResponseEntity.ok(responseAllProduct);
    }

    @PostMapping("/getbyid")
    public ResponseEntity<ResponseProduct> getProductById(@Valid @RequestBody RequeestByIDProductDTO requestProduct) {
        ResponseProduct responseProduct = productService.getProductById(requestProduct);
        return ResponseEntity.ok(responseProduct);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseProduct> updateProduct(@Valid @RequestBody RequestProduct requestProduct) {
        ResponseProduct responseProduct = productService.updateProduct(requestProduct);
        return ResponseEntity.ok(responseProduct);
    }


    @PostMapping("/updateQuantityList")
    public ResponseEntity<ResponseProduct> updateQuantityList(@Valid @RequestBody RequestupdateQuantity requestupdateQuantity) {
        ResponseProduct responseProduct = productService.updateQuantityList(requestupdateQuantity);
        return ResponseEntity.ok(responseProduct);
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseProduct> deleteProduct(@Valid @RequestBody RequeestByIDProductDTO requestProduct) {
        ResponseProduct responseProduct = productService.deleteProduct(requestProduct);
        return ResponseEntity.ok(responseProduct);
    }
}
