package com.senzhikong.config;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @author shu
 */
public interface BaseConfigConstants extends Serializable {
    /**
     * 获取配置编号
     * @return 配置编号
     */
    String getCode();

    /**
     * 获取配置描述
     * @return 配置描述
     */
    String getDescription();

    /**
     * 获取配置类型
     * @return 配置类型
     */
    String getType();

    /**
     * 获取是否隐秘
     * @return 是否隐秘
     */
    boolean isSecret();

    /**
     * 获取配置内容
     * @return 配置内容
     */
    Object getData();
}
