package com.example.gotogether.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "passport_last_name", nullable = false)
    private String passportLastName;

    @Column(name = "passport_first_name", nullable = false)
    private String passportFirstName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "birthday", nullable = false)
    private String birthday;

    @Column(name = "gender", nullable = false)
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_type")
    private Grouping type;
    @Column(name = "sns")
    private String sns;
    @Column(name = "delete_check")
    private String deleteCheck;

    @Column(name = "role", nullable = false)
    private String role;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public void updatePassword(String password) {
        this.password = password;
    }

    @Builder
    public User(String email, String password, String name, String birthday, String passportLastName, String passportFirstName, String phoneNumber, String role, String gender, String sns, String deleteCheck) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthday = birthday;
        this.passportLastName = passportLastName;
        this.passportFirstName = passportFirstName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.role = role;
        this.sns = sns;
        this.deleteCheck = deleteCheck;
    }

    public void update(String changePassword, String phoneNumber) {
        this.password = changePassword;
        this.phoneNumber = phoneNumber;
    }

}
