package com.vanmanagement.vmp.users;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNullApi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long seq;

    private String name;

    private String email;

    private String password;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createAt;


    public UserEntity(Long seq, String name, String email, String password, LocalDateTime lastLoginAt, LocalDateTime createAt) {
        this.seq = seq;
        this.name = name;
        this.email = email;
        this.password = password;
        this.lastLoginAt = lastLoginAt;
        this.createAt = createAt;
    }


}
