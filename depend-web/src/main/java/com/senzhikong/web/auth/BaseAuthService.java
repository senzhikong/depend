package com.senzhikong.web.auth;

/**
 * @author shu
 */
public interface BaseAuthService {
    LoginAccountModel findLoginToken(String token);
}
