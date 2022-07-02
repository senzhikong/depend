package com.senzhikong.web.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author shu
 */
@Data
public class LoginAccountModel implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Long accountId;
    private List<Long> roleIds;
    private List<String> roles;
    private List<Long> permissionIds;
    private List<String> permission;
    private String token;
    private Date loginTime;
}
