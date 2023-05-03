package com.vanmanagement.vmp.users;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.google.common.base.Preconditions.checkNotNull;

@Data
@NoArgsConstructor
public class LoginRequest {
    @NotNull(message = "email must be not null")
    @NotBlank(message = "email must be provided")
    @Size(min = 4, max = 50, message = "address length must be between 4 and 50 characters")
    @javax.validation.constraints.Email
    private String email;

    @NotNull(message = "password must be not null")
    @NotBlank(message = "password must be provided")
    private String password;

}
