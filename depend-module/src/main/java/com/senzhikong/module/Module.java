package com.senzhikong.module;


import com.senzhikong.config.BaseConfigConstants;

public interface Module extends InitializeBean {

    String getModuleCode();

    String getModuleName();

    String[] getCacheNames();

    BaseConfigConstants[] getConfigConstants();
}
