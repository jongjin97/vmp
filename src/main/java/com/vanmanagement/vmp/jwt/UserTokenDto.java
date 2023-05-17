package com.vanmanagement.vmp.jwt;

import com.vanmanagement.vmp.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTokenDto {

    private UserEntity userEntity;

    private String token;
}
