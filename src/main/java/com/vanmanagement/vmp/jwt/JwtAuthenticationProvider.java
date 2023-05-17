package com.vanmanagement.vmp.jwt;

import com.vanmanagement.vmp.users.Role;
import com.vanmanagement.vmp.users.UserEntity;
import com.vanmanagement.vmp.users.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.apache.commons.lang3.ClassUtils.isAssignable;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    public JwtAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    //Authentication 객체를 받아 JwtAuthenticationToken 형태로 변환하여 processUserAuthentication 메소드를 호출합니다.
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        return processUserAuthentication(
                String.valueOf(authenticationToken.getPrincipal()),
                authenticationToken.getCredentials()
        );
    }
    //주어진 이메일과 패스워드를 이용해 인증 작업을 수행
    //UserService 객체에서 login 메소드를 호출하여 이메일과 패스워드에 해당하는 유저 정보를 반환
    private Authentication processUserAuthentication(String email, String password) {
        try {
            UserEntity user = userService.login(email, password).getUserEntity();
            JwtAuthenticationToken authenticated =
                    new JwtAuthenticationToken(
                            new JwtAuthentication(user.getSeq(), user.getName()),
                            null,
                            createAuthorityList(user.getRole())
                    );
            authenticated.setDetails(user);
            return authenticated;
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return isAssignable(JwtAuthenticationToken.class, authentication);
    }

}