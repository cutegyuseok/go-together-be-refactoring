package com.example.gotogether.cart.controller;


import com.example.gotogether.auth.dto.UserDTO;
import com.example.gotogether.cart.dto.CartDTO;
import com.example.gotogether.cart.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"장바구니 서비스"}, description = "장바구니 추가, 장바구니수정, 장바구니 삭제, 장바구니 조회")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {
    private final CartService cartService;


    @PostMapping("/cart")
    @ApiOperation(value = "장바구니 상품 추가", notes = "상품ID를 통해 장바구니에 상품을 추가한다. \n\n" + "code: 201 장바구니 추가 성공, code: 400 발생하는 원린 body로 출력, code: 401 로그인X , code: 500 서버에러")
    public ResponseEntity<?> addCart(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @RequestBody CartDTO.AddCartReqDTO addCartReqDTO) {
        return cartService.addCart(userAccessDTO, addCartReqDTO);
    }

    @DeleteMapping("/cart")
    @ApiOperation(value = "장바구니 상품 삭제", notes = "장바구니 안에 있는 상품들을 삭제한다. \n\n" + "code: 200 장바구니 내 상품들 삭제 성공, code: 204 삭제 대상이 장바구니에 없는 상품, code: 400 잘못된 요청 형식이나 파라미터가 전달, code: 500 서버에러")
    public ResponseEntity<?> deletedCart(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @RequestBody List<Long> cartId) {
        return cartService.deleteCart(userAccessDTO, cartId);
    }

    @PatchMapping("/cart/{cartId}")
    @ApiOperation(value = "내 장바구니 수정", notes = "회원의 장바구니를 수정한다. \n\n" + "code: 200 장바구니 수정 성공, code: 400 발생하는 원린 body로 출력, code: 404 장바구니가 없음, code: 500 서버에러")
    public ResponseEntity<?> updatedCart(@PathVariable Long cartId, @RequestBody CartDTO.UpdateCartReqDTO updateCartReqDTO) {
        return cartService.updateCart(cartId, updateCartReqDTO);
    }

    @GetMapping("/cart")
    @ApiOperation(value = "내 장바구니 목록", notes = "회원의 장바구니 목록을 확인한다. \n\n" + "code: 200 장바구니 삭제 성공, code: 400 장바구니가 없음, code: 500 서버에러")
    public ResponseEntity<?> findAllCart(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO) {
        return cartService.getCartList(userAccessDTO);
    }

}
