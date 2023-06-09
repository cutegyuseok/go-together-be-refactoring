package com.example.gotogether.category.controller;

import com.example.gotogether.category.dto.CategoryDTO;
import com.example.gotogether.category.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"관리자 카테고리 서비스"}, description = "카테고리 생성, 수정, 삭제")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping("/")
    @ApiOperation(value = "카테고리 생성", notes = "이름 대(1),중(2),소(3) 구분, 부모 카테고리 입력 필요(대분류의 경우 아무값 상관 X).\n\n" +
            "code: 201 카테고리 생성됨, 406 이미 존재하는 이름, 400 depth 가 부적절함, 404 부모 카테고리가 없음")
    public ResponseEntity<?> makeCate(@RequestBody CategoryDTO.MakeCategory dto) {
        return categoryService.makeCate(dto);
    }

    @PatchMapping("/{categoryId}")
    @ApiOperation(value = "카테고리 이름 수정", notes = "카테고리 아이디와 카테고리 새로운 이름 입력.\n\n" +
            "code: 200 수정됨, 406 이미 존재하는 이름, 404 해당 카테고리 ID 가 없음")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO.UpdateCategory dto) {
        return categoryService.updateCate(categoryId, dto);
    }

    @DeleteMapping("/{categoryId}")
    @ApiOperation(value = "카테고리 삭제", notes = "카테고리 아이디를 입력하면 삭제.\n\n" +
            "code: 200 삭제됨, 404 해당 카테고리 ID 가 없음, 406 자녀 카테고리 먼저 삭제 필요.")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        return categoryService.deleteCate(categoryId);
    }


}
