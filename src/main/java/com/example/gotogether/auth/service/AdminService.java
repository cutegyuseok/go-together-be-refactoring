package com.example.gotogether.auth.service;

import com.example.gotogether.auth.dto.UserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<?> setUserToAdmin(String email);

    ResponseEntity<?> setAdminToUser(String email);

    ResponseEntity<?> findUserList(int page);

    ResponseEntity<?> findUser(Long id);

    ResponseEntity<?> updateUserInfo(Long userId, UserDTO.PatchUserByAdminReqDTO dto);
}