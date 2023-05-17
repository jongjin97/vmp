package com.vanmanagement.vmp.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class CustomTokenService {

    private final Jwt jwt;


    private final TokenRepository tokenRepository;

    public CustomTokenService(Jwt jwt, TokenRepository tokenRepository) {
        this.jwt = jwt;
        this.tokenRepository = tokenRepository;
    }
    public String refreshAccessToken(String expiredAccessToken) {
        DecodedJWT decodedJWT = JWT.decode(expiredAccessToken);
        if (decodedJWT != null && decodedJWT.getClaims() != null) {
            // 사용자 정보나 권한 등을 가져올 수 있습니다.
            Map<String, Claim> clames = decodedJWT.getClaims();

            // 저장된 Refresh Token을 가져옵니다.
            RefreshEntity storedRefreshToken = getStoredRefreshToken(clames.get("userKey").asLong());

            // 만료된 Access Token에 저장된 Refresh Token과 저장된 Refresh Token을 비교합니다.
            if (storedRefreshToken != null && storedRefreshToken.getExpires_at().isAfter(LocalDateTime.now())) {
                // Access Token을 새로 생성하여 리턴합니다.
                String newAccessToken = jwt.create(Jwt.Claims.of(clames.get("userKey").asLong()
                        , clames.get("name").toString()
                        , clames.get("roles").asArray(String.class)));
                return newAccessToken;
            }
        }
        return null;
    }

    // subject를 이용하여 Refresh Token을 저장하는 코드
    private RefreshEntity getStoredRefreshToken(Long subject) {
        // DB 또는 Memory 등에 저장된 Refresh Token을 가져와 리턴합니다.
        RefreshEntity token = tokenRepository.findByUser_Seq(subject);

        return token;
    }

}
