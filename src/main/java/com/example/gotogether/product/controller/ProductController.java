package com.example.gotogether.product.controller;

import com.example.gotogether.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Api(tags = {"상품 서비스"}, description = "상품 키워드 검색, 카테고리로 상품 검색)")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/categories/{categoryId}")
    @ApiOperation(value = "카테고리로 상품 검색", notes = "해당 카테고리와 무한 하위 카테고리까지 관련된 상품 반환. \n\n" +
            " 요청시 sort 는 asc(낮은 가격 순), desc(높은 가격 순), recent 또는 안보내도됨(required X)-> 최신순. \n\n" +
            "dateOption 은 출발일 입력 ex: 2023-03-28 \n\n" +
            "endDateOption 은 도착일 입력 ex: 2023-03-28 \n\n" +
            "people 은 함께 갈 인원수 \n\n" +
            "code: 200 상품 목록 조회 성공, 204 표시할 상품 없음, 400 잘못된 페이지 사이즈 요청 또는 잘못된 날짜 입력, 404 해당 카테고리가 존재하지 않음. 500 서버에러 ")
    public ResponseEntity<?> findProductByCategory(@PathVariable Long categoryId, @RequestParam(required = false, defaultValue = "1") int page,
                                                   @RequestParam(required = false,defaultValue = "recent") String sort,
                                                   @RequestParam(required = false,defaultValue = "1999-08-31") String dateOption,
                                                   @RequestParam(required = false, defaultValue = "2999-12-25") String endDateOption,
                                                   @RequestParam(required = false,defaultValue = "0") int people) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        try {
            startDate = LocalDate.parse(dateOption).minusDays(1);
            endDate = LocalDate.parse(endDateOption).plusDays(1);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return productService.findProductByCategory(categoryId, page,sort,startDate,endDate,people);
    }

    @GetMapping("/search")
    @ApiOperation(value = "키워드로 상품 검색", notes = "해당 키워드와 관련된 상품 반환.\n\n" +
            " 요청시 sort 는 asc(낮은 가격 순), desc(높은 가격 순), recent 또는 안보내도됨(required X)-> 최신순. \n\n" +
            "dateOption 은 출발일 입력 ex: 2023-03-28 \n\n" +
            "endDateOption 은 도착일 입력 ex: 2023-03-28 \n\n" +
            "people 은 함께 갈 인원수 \n\n" +
            "code: 200 상품 목록 조회 성공, 204 표시할 상품 없음, 400 잘못된 페이지 사이즈 요청 또는 잘못된 날짜 입력, 500 서버에러 ")
    public ResponseEntity<?> searchProductByKeyword(@RequestParam(required = true) String keyword,
                                                    @RequestParam(required = false, defaultValue = "recent") String sort,
                                                    @RequestParam(required = false, defaultValue = "1") int page,
                                                    @RequestParam(required = false, defaultValue = "1999-08-31") String dateOption,
                                                    @RequestParam(required = false, defaultValue = "2999-12-25") String endDateOption,
                                                    @RequestParam(required = false, defaultValue = "0") int people) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        try {
            startDate = LocalDate.parse(dateOption).minusDays(1);
            endDate = LocalDate.parse(endDateOption).plusDays(1);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return productService.findProductByKeyword(keyword, page, sort, startDate, endDate,people);
    }

    @GetMapping("/detail/{productId}")
    @ApiOperation(value = "상품 상세 정보", notes = "사용자에게 상품 상세 정보를 보여줍니다.\n\n" + "code: 200 상품 상세 조회 성공, code: 404 해당 상품이 존재하지 않음. code:500 서버에러 ")
    public ResponseEntity<?> oneProductForUser(@PathVariable Long productId) {
        return productService.findDetailProduct(productId);
    }

    @GetMapping("/related/{productId}")
    @ApiOperation(value = "관련 상품 리스트", notes = "사용자에게 관련 상품들을 보여줍니다.\n\n" + "code: 200 관련 상품 조회 성공, code: 404 관련 상품이 존재하지 않음. code:500 서버에러 ")
    public ResponseEntity<?> ProductsByType(@PathVariable Long productId) {
        return productService.getProductByType(productId);
    }

}
