package com.vanmanagement.vmp.certification;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class IamportService {
    private final String apiUrl = "https://api.iamport.kr";
    private final String apiKey = "1831287181004585";
    private final String apiSecret = "amXXujhIyNeRp57hsRaRdm3ECpnc2LqMW6eR2wCUsPoSksOvgkuKBdQxgiTdm1ZzyfPidM1cTz2Qtdpx";

    private final RestTemplate restTemplate;

    public IamportService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public IamportCertificationResponse getCertification(String impUid) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 토큰 발급
        Map<String, String> getTokenMap = new HashMap<>();
        getTokenMap.put("imp_key", apiKey);
        getTokenMap.put("imp_secret", apiSecret);
        HttpEntity<Map<String, String>> getTokenRequest = new HttpEntity<>(getTokenMap, headers);
        ResponseEntity<Map> getTokenResponse = restTemplate.postForEntity(
                "https://api.iamport.kr/users/getToken", getTokenRequest, Map.class);
        Map<String, Map> reponsebody = (Map<String, Map>) getTokenResponse.getBody().get("response");
        String accessToken = String.valueOf(reponsebody.get("access_token"));

        // 인증정보 조회
        headers.set("Authorization", accessToken);
        HttpEntity<String> getCertificationRequest = new HttpEntity<>(headers);
        ResponseEntity<IamportCertificationResponse> getCertificationResponse = restTemplate.exchange(
                "https://api.iamport.kr/certifications/" + impUid, HttpMethod.GET,
                getCertificationRequest, IamportCertificationResponse.class);
        IamportCertificationResponse certificationInfo = getCertificationResponse.getBody();

        return certificationInfo;
    }

}
