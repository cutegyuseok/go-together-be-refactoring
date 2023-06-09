package com.example.gotogether.auth.dto;

import com.example.gotogether.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class UserDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ApiModel(value = "로그인")
    public static class LoginReqDTO {

        @ApiModelProperty(value = "이메일 ", required = true)
        private String userEmail;

        @ApiModelProperty(value = "비밀번호 ", required = true)
        private String userPassword;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ApiModel(value = "토큰에 담길 정보")
    public static class UserAccessDTO {
        @ApiModelProperty(value = "이메일 ", required = true)
        private String email;

        @ApiModelProperty(value = "권한 ", required = true)
        private String role;

        public UserAccessDTO(Claims claims) {
            this.email = claims.get("email", String.class);
            this.role = claims.get("role", String.class);
        }

        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singleton(new SimpleGrantedAuthority(this.role));
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "회원가입")
    public static class SignupReqDTO {
        @ApiModelProperty(value = "이름", required = true)
        private String userName;
        @ApiModelProperty(value = "이메일", required = true)
        private String userEmail;
        @ApiModelProperty(value = "비밀번호", required = true)
        private String userPassword;
        @ApiModelProperty(value = "비밀번호 확인", required = true)
        private String passwordConfirmation;
        @ApiModelProperty(value = "여권에 적힌 이름", required = true)
        private String passportFirstName;
        @ApiModelProperty(value = "여권에 적힌 성", required = true)
        private String passportLastName;
        @ApiModelProperty(value = "전화번호", required = true)
        private String userPhoneNumber;
        @ApiModelProperty(value = "생년월일", required = true)
        private String userBirth;
        @ApiModelProperty(value = "성별", required = true)
        private String userGender;
        @ApiModelProperty(value = "sns 로그인 여부", required = true)
        private String sns;


        public User toEntity() {

            return User.builder()
                    .name(userName)
                    .email(userEmail)
                    .password(userPassword)
                    .passportFirstName(passportFirstName)
                    .passportLastName(passportLastName)
                    .phoneNumber(userPhoneNumber)
                    .birthday(userBirth)
                    .gender(userGender)
                    .sns(sns)
                    .role("ROLE_USER")
                    .deleteCheck("available")
                    .build();
        }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "회원정보수정 입력")
    public static class PatchUserReqDTO {

        @ApiModelProperty(value = "기존 비밀번호", required = true)
        private String userPassword;
        @ApiModelProperty(value = "새로운 비밀번호")
        private String changePassword;
        @ApiModelProperty(value = "새로운 비밀번호 재입력")
        private String passwordConfirmation;
        @ApiModelProperty(value = "전화번호", required = true)
        private String userPhoneNumber;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "회원정보수정 출력")
    public static class PatchUserResDTO {

        @ApiModelProperty(value = "이름", required = true)
        private String userName;
        @ApiModelProperty(value = "이메일", required = true)
        private String userEmail;
        @ApiModelProperty(value = "여권에 적힌 이름", required = true)
        private String passportFirstName;
        @ApiModelProperty(value = "여권에 적힌 성", required = true)
        private String passportLastName;
        @ApiModelProperty(value = "전화번호", required = true)
        private String userPhoneNumber;
        @ApiModelProperty(value = "생년월일", required = true)
        private String userBirth;
        @ApiModelProperty(value = "성별", required = true)
        private String userGender;
        @ApiModelProperty(value = "여행 유형", required = true)
        private String userType;


        public PatchUserResDTO(User user) {
            this.userName = user.getName();
            this.userEmail = user.getEmail();
            this.userBirth = user.getBirthday();
            this.userPhoneNumber = user.getPhoneNumber();
            this.userGender = user.getGender();
            this.passportFirstName = user.getPassportFirstName();
            this.passportLastName = user.getPassportLastName();
            if (user.getType() != null) {
                this.userType = user.getType().getUserType();
            }
        }
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "회원 탈퇴")
    public static class DeleteUserReqDTO {

        @ApiModelProperty(value = "비밀번호 ", required = true)
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "이메일만 사용하는 dto")
    public static class EmailOnly {

        @ApiModelProperty(value = "이메일 ", required = true)
        private String email;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "회원리스트 출력")
    public static class UserListDto {
        private Long userId;
        private String userEmail;
        private String userName;
        private String userRole;

        public UserListDto(User user) {
            this.userId = user.getUserId();
            this.userEmail = user.getEmail();
            this.userName = user.getName();
            this.userRole = user.getRole();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "사용자 여행 유형 dto")
    public static class UserType {

        @ApiModelProperty(value = "사용자 여행 유형 ", required = true)
        private String userType;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class UserDetailsForAdmin {
        private Long userId;
        private String userName;
        private String userEmail;
        private String userPhoneNumber;
        private String userBirthday;
        private String passportFirstName;
        private String passportLastName;
        private String userGender;
        private String userType;
        private String sns;
        private String deleteCheck;
        private String userRole;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;

        public UserDetailsForAdmin(User user) {
            this.userId = user.getUserId();
            this.userName = user.getName();
            this.userEmail = user.getEmail();
            this.userPhoneNumber = user.getPhoneNumber();
            this.userBirthday = user.getBirthday();
            this.passportFirstName = user.getPassportFirstName();
            this.passportLastName = user.getPassportLastName();
            this.userGender = user.getGender();
            if (user.getType() != null) {
                this.userType = user.getType().getUserType();
            }
            this.sns = user.getSns();
            this.deleteCheck = user.getDeleteCheck();
            this.userRole = user.getRole();
            this.createdDate = user.getCreatedDate();
            this.updatedDate = user.getUpdatedDate();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "관리자의 회원정보수정 입력", description = "변경 할 값만 입력")
    public static class PatchUserByAdminReqDTO {
        @ApiModelProperty(value = "사용자 이름 입력.")
        private String userName;
        @ApiModelProperty(value = "사용자 전화번호 입력")
        private String userPhoneNumber;
        @ApiModelProperty(value = "사용자 생년월일 입력")
        private String userBirthday;
        @ApiModelProperty(value = "사용자 여권에 적힌 이름")
        private String passportFirstName;
        @ApiModelProperty(value = "사용자 여권에 적힌 성")
        private String passportLastName;
        @ApiModelProperty(value = "사용자 성별 입력\n\n male or female")
        private String userGender;
        @ApiModelProperty(value = "사용자 탈퇴여부 입력\n\n available or withdraw")
        private String deleteCheck;
    }
}

