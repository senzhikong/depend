package com.senzhikong.module;


import com.senzhikong.config.BaseConfigConstants;

/**
 * @author shu
 */
public interface Module extends InitializeBean {

    /**
     * 获取模块编号
     * @return 模块编号
     */
    String getModuleCode();

    /**
     * 获取模块名称
     * @return 模块名称
     */
    String getModuleName();

    /**
     * 获取模块缓存名称数组
     * @return 缓存名称数组
     */
    String[] getCacheNames();

    /**
     * 获取配置项
     * @return 配置项数组
     */
    BaseConfigConstants[] getConfigConstants();
}
