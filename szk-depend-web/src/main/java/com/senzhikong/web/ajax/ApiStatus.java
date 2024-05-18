package com.senzhikong.web.ajax;

import lombok.Getter;

/**
 * 业务编码
 *
 * @author Shu.zhou
 */
@Getter
public enum ApiStatus {
    /**
     * 成功
     */
    OK(0, "操作成功"),
    METHOD_NOT_SUPPORTED(405, "请求方式错误"),
    INTERNAL_SERVER_ERROR(500, "系统错误"),
    FORBIDDEN(403, "禁止访问"),
    ERROR(600, "错误"),
    PARAMS_VALIDATE_ERROR(605, "验证错误"),
    NOT_PERMISSION(602, "权限不足"),
    NOT_LOGIN(601, "用户未登录");

    private final int value;
    private final String message;

    ApiStatus(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int value() {
        return this.value;
    }

    public String message() {
        return message;
    }
}
