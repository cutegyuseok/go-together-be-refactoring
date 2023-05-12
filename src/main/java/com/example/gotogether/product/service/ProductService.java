package com.example.gotogether.product.service;

import com.example.gotogether.product.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface ProductService {

    ResponseEntity<?> createProduct(ProductDTO.ProductCreateReqDTO productReqDTO);

    ResponseEntity<?> deleteProduct(Long productId);

    ResponseEntity<?> getAllProducts(int page);

    ResponseEntity<?> updateProduct(Long productId, ProductDTO.ProductUpdateReqDTO productReqDTO);

    ResponseEntity<?> findDetailProduct(Long productId);

    ResponseEntity<?> findProductByCategory(Long categoryId, int page, String sort, LocalDate startDate,LocalDate endDate,int people);

    ResponseEntity<?> findProductByKeyword(String keyword, int page, String sort, LocalDate startDate,LocalDate endDate, int people);

    ResponseEntity<?> findPopularProducts(Long categoryId);

    ResponseEntity<?> getProductByType(Long productId);
}
