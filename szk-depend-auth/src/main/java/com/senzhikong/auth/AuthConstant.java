package com.senzhikong.auth;

/**
 * @author shu.zhou
 */
public interface AuthConstant {

    String X_HEADER_USER_ID = "x-header-user-id";

    String CACHE_LOGIN_MODEL = "login-model-";
    String CACHE_LOGIN_ACCOUNT_TOKEN = "login-account-token-";
    String CACHE_LOGIN_ACCOUNT_APP_TOKEN = "login-account-app-token-";
    String CACHE_AUTH_FILTER_LIST = "auth_filter_list";
    String CACHE_AUTH_USER_ROLE = "auth_user_role_";
    String CACHE_AUTH_ROLE_PERMISSION = "auth_role_permission_";
    String CACHE_QRCODE_BIND_MODEL = "qrcode-bind-model-";
    String CACHE_QRCODE_LOGIN_MODEL = "qrcode-login-model-";
}
