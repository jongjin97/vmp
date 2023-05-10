package com.vanmanagement.vmp.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
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

    @Column(name = "name")
    private String name;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "email")
    private String email;

    @Column(name = "addr")
    private String addr;

    @Column(name = "extra_addr")
    private String extraAddr;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "point")
    private Long point;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "password")
    private String password;


}
