package com.example.gotogether.product.controller;

import com.example.gotogether.product.dto.ProductDTO;
import com.example.gotogether.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"관리자 상품 서비스"}, description = "상품 추가, 상품수정, 상품삭제(숨김)")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminProductController {

    private final ProductService productService;

    @PostMapping("/products")
    @ApiOperation(value = "상품 추가", notes = "상품을 추가합니다.\n\n" + "code: 200 상품 추가 성공, code: 400 같은 이름의 상품이 존재, code: 403 권한 없음, code: 404 선택한 카테고리의 종류가 없음 또는 해당 그릅의 타입이 없음, code: 500 서버에러 ")
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO.ProductCreateReqDTO productCreateReqDTO) {

        return productService.createProduct(productCreateReqDTO);
    }

    //상품 삭제
    @DeleteMapping("/products/{productId}")
    @ApiOperation(value = "상품 삭제(숨김)", notes = "상품을 삭제처리(숨김) 합니다. \n\n" + "code: 200 상품 숨김(삭제) 성공, code: 403 권한 없음, code: 404 해당 상품이 없음, code: 500 서버에러")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }

    @PatchMapping("/products/{productId}")
    @ApiOperation(value = "상품 수정", notes = "상품을 수정(옵션 제외) 합니다.\n\n" + "code: 200 상품 수정 성공, code: 403 권한 없음, code: 400 해당 상품이 없음,code: 404 선택한 카테고리의 종류가 없음 또는 해당 타입의 유형 없음, code: 500 서버에러")
    public ResponseEntity<?> updateProducts(@PathVariable Long productId, @RequestBody ProductDTO.ProductUpdateReqDTO productUpdateReqDTO) {
        System.out.println(productUpdateReqDTO.toString());
        return productService.updateProduct(productId, productUpdateReqDTO);
    }


    // 제품 전체목록 보기
    @GetMapping("/products")
    @ApiOperation(value = "상품 전체 목록 조회", notes = "전체 상품들의 목록들을 조회 합니다.\n\n" + "code: 200 상품 전체 목록 조회 성공, code: 204 조회될 상품이 없음, code: 403 권한 없음, code:500 서버에러 ")
    public ResponseEntity<?> allProducts(@RequestParam(required = false, defaultValue = "1") int page) {
        return productService.getAllProducts(page);
    }


    // 제품 상세 보기
    @GetMapping("/products/detail/{productId}")
    @ApiOperation(value = "상품 상세 정보", notes = "상품 상세 정보를 보여줍니다.\n\n" + "code: 200 상품 상세 정보 조회 성공, code: 403 권한 없음, code: 404 해당 상품이 없음, code: 500 서버에러")
    public ResponseEntity<?> oneProduct(@PathVariable Long productId) {
        return productService.findDetailProduct(productId);
    }


}
