package com.vanmanagement.vmp.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vanmanagement.vmp.errors.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Date;
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final CustomTokenService customTokenService;

    public TokenInterceptor(CustomTokenService customTokenService) {
        this.customTokenService = customTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization"); // Access Token을 Header에서옵니다.
        if (token != null && token.startsWith("Bearer ")) {
            try {
                token = token.substring("Bearer ".length());
                DecodedJWT decodedJWT = JWT.decode(token);

                if (decodedJWT.getExpiresAt().before(Date.from(Instant.now()))) {

                    String newAccessToken = customTokenService.refreshAccessToken(token);
                    if (newAccessToken == null) {
                        // Refresh Token 검증에 실패하였거나, 발급한 Access Token이 존재하지 않을 경우 예외를 throw합니다.
                        throw new JWTDecodeException("Invalid refresh token.");
                    }
                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                }

                return true;
            } catch (JWTDecodeException ex) {
                // Access Token 파싱에 실패하였을 경우 예외를 throw합니다.
                throw new JWTDecodeException("Invalid access token.");
            }
        }

        // Authentication이 필요한 API에 접근할 때, Access Token이 없을 경우 예외를 throw합니다.
        throw new UnauthorizedException("Authorization header not found.");
    }

}