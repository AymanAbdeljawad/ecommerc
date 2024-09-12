package com.product_service.service;

import com.product_service.common.mapper.ProductMapper;
import com.product_service.dto.*;
import com.product_service.entity.Product;
import com.product_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseProduct createProduct(RequestProduct requestProduct) {
        ProductDTO productDTO = requestProduct.getProductDTO();
        if (productDTO == null) {
            return new ResponseProduct(1, "Invalid Data", "ProductDTO cannot be null.", "Invalid data provided", null);
        }
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            return new ResponseProduct(1, "Invalid Data", "Product name cannot be null or empty.", "Invalid data provided", null);
        }
        if (productDTO.getPrice() == null || productDTO.getPrice() <= 0) {
            return new ResponseProduct(1, "Invalid Data", "Product price must be a positive value.", "Invalid data provided", null);
        }
        Product product = ProductMapper.convertToEntity(productDTO);
        try {
            Product savedProduct = productRepository.save(product);
            ProductDTO savedProductDTO = ProductMapper.convertToDTO(savedProduct);
            return new ResponseProduct(0, "Success", "Product created successfully.", "Product created successfully", savedProductDTO);
        } catch (Exception e) {
            return new ResponseProduct(1, "Error", "Failed to create product. Please try again later.", "Error occurred", null);
        }
    }


    public ResponseAllProduct getAllProducts() {
        List<ProductDTO> productDTOS;
        try {
            productDTOS = productRepository.findAll()
                    .stream()
                    .map(ProductMapper::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseAllProduct(0, "Success", "Products retrieved successfully.", "Products retrieved successfully", productDTOS);
        } catch (Exception e) {
            return new ResponseAllProduct(1, "Error", "Failed to retrieve products. Please try again later.", "Error occurred", null);
        }
    }


    public ResponseProduct getProductById(RequeestByIDProductDTO requestByIDProductDTO) {
        Long productId = requestByIDProductDTO.getProductId();
        ProductDTO productDTO;
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isEmpty()) {
                return new ResponseProduct(1, "Not Found", "Product not found with id " + productId, "Product not found");
            }
            Product product = optionalProduct.get();
            productDTO = ProductMapper.convertToDTO(product);
            return new ResponseProduct(0, "Success", "Product retrieved successfully.", "Product retrieved successfully", productDTO);
        } catch (Exception e) {
            return new ResponseProduct(1, "Error", "Failed to retrieve product. Please try again later.", "Error occurred");
        }
    }



    public ResponseProduct updateProduct(RequestProduct requestProduct) {
        ProductDTO productDTO = requestProduct.getProductDTO();
        Long id = productDTO.getProductId();
        try {
            if (!productRepository.existsById(id)) {
                return new ResponseProduct(1, "Not Found", "Product not found with ID " + id, "Product not found", null);
            }
            Product product = ProductMapper.convertToEntity(productDTO);
            product.setProductId(id);
            Product updatedProduct = productRepository.save(product);
            ProductDTO updatedProductDTO = ProductMapper.convertToDTO(updatedProduct);
            return new ResponseProduct(0, "Success", "Product updated successfully.", "Product updated successfully", updatedProductDTO);
        } catch (Exception e) {
            return new ResponseProduct(1, "Error", "Failed to update product. Please try again later.", "Error occurred", null);
        }
    }


    @Transactional
    public ResponseProduct updateQuantityList(RequestupdateQuantity requestupdateQuantity) {
        try {
            List<QuantityDTO> quantityDTOList = requestupdateQuantity.getQuantityDTOList();
            List<Long> productIds = quantityDTOList.stream()
                    .map(QuantityDTO::getProductId)
                    .collect(Collectors.toList());
            Map<Long, Product> productMap = productRepository.findAllById(productIds)
                    .stream()
                    .collect(Collectors.toMap(Product::getProductId, p -> p));
            StringBuilder errorMessages = new StringBuilder();
            boolean hasErrors = false;
            for (QuantityDTO quantityDTO : quantityDTOList) {
                Long productId = quantityDTO.getProductId();
                Product product = productMap.get(productId);

                if (product == null) {
                    errorMessages.append("Product not found with ID ").append(productId).append(". ");
                    hasErrors = true;
                    continue;
                }
                Integer requestedQuantity = quantityDTO.getQuantity();
                Integer availableQuantity = product.getQuantity();

                if (requestedQuantity > availableQuantity) {
                    errorMessages.append("Insufficient quantity for product ID ").append(productId)
                            .append(". Requested: ").append(requestedQuantity)
                            .append(", Available: ").append(availableQuantity).append(". ");
                    hasErrors = true;
                } else {
                    product.setQuantity(availableQuantity - requestedQuantity);
                    productRepository.save(product);
                }
            }
            if (hasErrors) {
                return new ResponseProduct(1, "Error", errorMessages.toString(), "Errors occurred", null);
            }
            List<ProductDTO> updatedProductDTOs = productMap.values().stream()
                    .map(ProductMapper::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseProduct(0, "Success", "Products updated successfully.", "Products updated successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseProduct(1, "Error", "Failed to update products. Please try again later.", "Error occurred", null);
        }
    }


//    public ResponseProduct updateProductList(List<RequestProduct> requestProduct) {
//        List<RequestProduct> requestProduct1 = requestProduct;
//        try {
//            for (RequestProduct requestProduct2 : requestProduct1) {
//                if (!productRepository.existsById(requestProduct2.getProductDTO().getProductId())) {
//                    return new ResponseProduct(1, "Not Found", "Product not found with ID " + id, "Product not found", null);
//                }
//                Product product
//                        = productRepository.findById(requestProduct2.getProductDTO().getProductId()).orElseThrow();
//                Integer Quantity = 0;
//                if (product.getQuantity() > requestProduct2.getProductDTO().getQuantity()) {
//                    Quantity = product.getQuantity() - requestProduct2.getProductDTO().getQuantity();
//                } else {
//                    Quantity = requestProduct2.getProductDTO().getQuantity() - product.getQuantity();
//
//                }
//                product.setQuantity(Quantity);
//                productRepository.save(product);
//            }
//            return new ResponseProduct(0, "Success", "Product updated successfully.", "Product updated successfully", updatedProductDTO);
//        } catch (Exception e) {
//            return new ResponseProduct(1, "Error", "Failed to update product. Please try again later.", "Error occurred", null);
//        }
//    }



//    @Transactional
//    public ResponseProduct updateQuantityList(RequestupdateQuantity requestupdateQuantity) {
//        try {
//            List<QuantityDTO> quantityDTOList = requestupdateQuantity.getQuantityDTOList();
//            List<Long> productIds = quantityDTOList.stream()
//
//                    .map(req -> req.getProductId())
//                    .collect(Collectors.toList());
//
//            Map<Long, Product> productMap = productRepository.findAllById(productIds)
//                    .stream()
//                    .collect(Collectors.toMap(Product::getProductId, p -> p));
//
//
//            for (QuantityDTO quantityDTO : quantityDTOList) {
//                Long productId = quantityDTO.getProductId();
//                Product product = productMap.get(productId);
//
//                if (product == null) {
//                    return new ResponseProduct(1, "Not Found", "Product not found with ID " + productId, "Product not found", null);
//                }
//
//                Integer newQuantity;
//
//                if (product.getQuantity() > quantityDTO.getQuantity()) {
//                    newQuantity = (product.getQuantity() - quantityDTO.getQuantity());
//                } else {
//                    return new ResponseProduct(1, "Error", "Failed to update products. Please try again later.", "Error occurred", null);
//                }
//                product.setQuantity(newQuantity);
//                productRepository.save(product);
//            }
//            List<ProductDTO> updatedProductDTOs = productMap.values().stream()
//                    .map(ProductMapper::convertToDTO)
//                    .collect(Collectors.toList());
//
//            return new ResponseProduct(0, "Success", "Products updated successfully.", "Products updated successfully", null);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseProduct(1, "Error", "Failed to update products. Please try again later.", "Error occurred", null);
//        }
//    }




    public ResponseProduct deleteProduct(RequeestByIDProductDTO requeestByIDProductDTO) {
        Long id = requeestByIDProductDTO.getProductId();
        try {
            if (!productRepository.existsById(id)) {
                return new ResponseProduct(1, "Not Found", "Product not found with ID " + id, "Product not found", null);
            }
            productRepository.deleteById(id);
            return new ResponseProduct(0, "Success", "Product deleted successfully.", "Product deleted successfully", null);
        } catch (Exception e) {
            return new ResponseProduct(1, "Error", "Failed to delete product. Please try again later.", "Error occurred", null);
        }
    }

}
