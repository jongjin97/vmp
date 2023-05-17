package com.vanmanagement.vmp.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.google.common.base.Preconditions.checkArgument;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull(message = "name must be not null")
    @NotBlank(message = "name must be provided")
    private String name;

    @NotNull(message = "email must be not null")
    @NotBlank(message = "email must be provided")
    @Size(min = 4, max = 50, message = "address length must be between 4 and 50 characters")
    @javax.validation.constraints.Email
    private String email;

    @NotNull(message = "birth must be not null")
    @NotBlank(message = "birth must be provided")
    private String birth;

    @NotNull(message = "password must be not null")
    @NotBlank(message = "password must be provided")
    private String password;

    @NotNull(message = "passwordConfirm must be not null")
    @NotBlank(message = "passwordConfirm must be provided")
    private String passwordConfirm;

    @NotNull(message = "phone must be not null")
    @NotBlank(message = "phone must be provided")
    private String phone;

//    @NotNull(message = "addr must be not null")
//    @NotBlank(message = "addr must be provided")
//    private String addr;
//
//    private String extraAddr;
//
//    @NotNull(message = "postcode must be not null")
//    @NotBlank(message = "postcode must be provided")
//    private String postcode;

    UserEntity toEntity(){
        return UserEntity.builder()
                .email(getEmail())
                .name(getName())
                .birth(LocalDate.parse(getBirth(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                .password(getPassword())
                .createAt(LocalDateTime.now())
                .phone(getPhone())
//                .addr(getAddr())
//                .extraAddr(getExtraAddr())
//                .postcode(getPostcode())
                .build();
    }
}
