package com.senzhikong.web.context;

import com.senzhikong.util.string.sign.Md5Util;

import java.util.UUID;

/**
 * 鉴权工具
 *
 * @author shu
 */
public interface MyWebContext {
    /**
     * 生成token
     *
     * @return token
     */
    default String generateToken() {
        return Md5Util.getInstance()
                .encode(UUID.randomUUID().toString() + System.currentTimeMillis(), "UTF-8",
                        "szk-login-token");
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    String getUserId();

    /**
     * 获取当前请求token
     *
     * @return token
     */
    String getToken();
}
