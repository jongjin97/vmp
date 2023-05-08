package com.vanmanagement.vmp.certification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IamportCertificationResponse {
    private int code;
    private String message;
    private ResponseData response;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseData {
        private String imp_uid;
        private String merchant_uid;
        private String pg_tid;
        private String pg_provider;
        private String name;
        private String gender;
        private int birth;
        private String birthday;
        private boolean foreigner;
        private String phone;
        private String carrier;
        private boolean certified;
        private long certified_at;
        private String unique_key;
        private String unique_in_site;
        private String origin;
        private boolean foreigner_v2;

    }
}


