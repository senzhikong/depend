package com.senzhikong.auth.domain;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author shu
 */
@Data
public class LoginUserModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String userId;
    private List<String> roleIds;
    private List<String> roles;
    private List<String> permissionIds;
    private List<String> permission;
    private List<String> urlFilters;
    private String token;
    private Date loginTime;
    private Long expire;
    private String userRsaPublicKey;
    private String userRsaPrivateKey;
    private String userAesKey;
    private String userAesIv;
    private String tokenAesKey;
    private String tokenAesIv;
}
