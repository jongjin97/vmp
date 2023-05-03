package com.vanmanagement.vmp.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;

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

    @NotNull(message = "password must be not null")
    @NotBlank(message = "password must be provided")
    private String password;

    @NotNull(message = "passwordConfirm must be not null")
    @NotBlank(message = "passwordConfirm must be provided")
    private String passwordConfirm;

    UserEntity toEntity(){
        return UserEntity.builder().email(getEmail())
                .name(getName())
                .password(getPassword())
                .createAt(LocalDateTime.now())
                .build();
    }
}