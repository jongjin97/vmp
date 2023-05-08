package com.vanmanagement.vmp.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long seq;

    private String name;

    private String birth;

    private String email;

    private String phone;

    private String addr;

    private String extraAddr;

    private String postcode;

    private String password;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createAt;


}
