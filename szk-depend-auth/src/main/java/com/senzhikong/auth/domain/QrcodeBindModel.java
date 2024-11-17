package com.senzhikong.auth.domain;

import lombok.Data;

/**
 * @author shu.zhou
 */
@Data
public class QrcodeBindModel {
    private String code;
    private String token;
    private String status;
    private String userId;
    private Long expireTime;
}
