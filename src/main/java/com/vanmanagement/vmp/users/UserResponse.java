package com.vanmanagement.vmp.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserResponse {

    private String name;

    private String email;

    private String phone;

    private Long point;
}
