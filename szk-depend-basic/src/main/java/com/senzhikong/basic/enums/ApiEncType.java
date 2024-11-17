package com.senzhikong.basic.enums;

import com.senzhikong.util.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author shu.zhou
 */

@Getter
@AllArgsConstructor
@Accessors(chain = true, fluent = true)
public enum ApiEncType implements BaseEnum {
    /**
     * 加密方式
     */
    NONE("none", "不加密"),
    RSA("rsa", "固定秘钥RSA"),
    AES("aes", "固定秘钥AES"),
    USER_AES("user_aes", "用户秘钥AES"),
    TOKEN_AES("token_aes", "凭证秘钥RSA"),
    USER_RSA("user_rsa", "用户秘钥RSA"),
//    TOKEN_RSA("token_rsa")
    ;

    private final String code;
    private final String desc;

}

