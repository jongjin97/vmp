package com.vanmanagement.vmp.jwt;

import com.vanmanagement.vmp.users.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime create_at;

    private LocalDateTime expires_at;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seq", nullable = false)
    private UserEntity user;

    public RefreshEntity(String token, LocalDateTime create, LocalDateTime expires, UserEntity user){
        this.token = token;
        this.create_at = create;
        this.expires_at = expires;
        this.user = user;
    }
}
