package com.vanmanagement.vmp.users;

import com.vanmanagement.vmp.security.Jwt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long seq;

    private String name;

    private String email;

    private int loginCount;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createAt;

    public User(String name, String email, String password) {
        this(null, name, email, password, 0, null, null);
    }

    public User(Long seq, String name, String email, String password, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {

        this.seq = seq;
        this.name = name;
        this.email = email;
        this.loginCount = loginCount;
        this.lastLoginAt = lastLoginAt;
        this.createAt = defaultIfNull(createAt, now());
    }

    public User(UserEntity userEntity) {
        this.seq = userEntity.getSeq();
        this.email = userEntity.getEmail();
        this.name = userEntity.getName();
        this.lastLoginAt = userEntity.getLastLoginAt();
        this.createAt = userEntity.getCreateAt();
    }

    public String newJwt(Jwt jwt, String[] roles) {
        Jwt.Claims claims = Jwt.Claims.of(seq, name, roles);
        return jwt.create(claims);
    }

    public void afterLoginSuccess() {
        loginCount++;
        lastLoginAt = now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(seq, user.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("name", name)
                .append("email", email)
                .append("loginCount", loginCount)
                .append("lastLoginAt", lastLoginAt)
                .append("createAt", createAt)
                .toString();
    }

//    static public class Builder {
//        private Long seq;
//        private String name;
//        private Email email;
//        private String password;
//        private int loginCount;
//        private LocalDateTime lastLoginAt;
//        private LocalDateTime createAt;
//
//        public Builder() {/*empty*/}
//
//        public Builder(User user) {
//            this.seq = user.seq;
//            this.name = user.name;
//            this.email = user.email;
//            this.password = user.password;
//            this.loginCount = user.loginCount;
//            this.lastLoginAt = user.lastLoginAt;
//            this.createAt = user.createAt;
//        }
//
//        public Builder seq(Long seq) {
//            this.seq = seq;
//            return this;
//        }
//
//        public Builder name(String name) {
//            this.name = name;
//            return this;
//        }
//
//        public Builder email(Email email) {
//            this.email = email;
//            return this;
//        }
//
//        public Builder password(String password) {
//            this.password = password;
//            return this;
//        }
//
//        public Builder loginCount(int loginCount) {
//            this.loginCount = loginCount;
//            return this;
//        }
//
//        public Builder lastLoginAt(LocalDateTime lastLoginAt) {
//            this.lastLoginAt = lastLoginAt;
//            return this;
//        }
//
//        public Builder createAt(LocalDateTime createAt) {
//            this.createAt = createAt;
//            return this;
//        }
//
//        public User build() {
//            return new User(seq, name, email, password, loginCount, lastLoginAt, createAt);
//        }
//    }

}
