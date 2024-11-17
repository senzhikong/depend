package com.senzhikong.auth.domain;

import lombok.Data;

/**
 * @author shu.zhou
 */
@Data
public class QrcodeLoginModel {
    private String code;
    private String token;
    private String userId;
    private String status;
    private Long expireTime;
}
