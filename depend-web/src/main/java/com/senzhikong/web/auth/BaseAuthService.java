package com.senzhikong.web.auth;

public interface BaseAuthService {
    LoginAccountModel findLoginToken(String token);
}
