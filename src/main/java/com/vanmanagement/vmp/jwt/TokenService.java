package com.vanmanagement.vmp.jwt;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void save(RefreshEntity refreshEntity, Long user_id){

        RefreshEntity findRefresh = tokenRepository.findByUser_Seq(user_id);

        if(findRefresh == null)
            tokenRepository.save(refreshEntity);
        else{
            findRefresh.setToken(refreshEntity.getToken());
            findRefresh.setCreate_at(refreshEntity.getCreate_at());
            findRefresh.setExpires_at(refreshEntity.getExpires_at());
        }
    }

}
