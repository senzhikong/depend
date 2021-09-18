package com.senzhikong.module;


import com.senzhikong.web.InitializeBean;

public interface Module extends InitializeBean {

    String getModuleCode();

    String getModuleName();

    String[] getCacheNames();

    BaseConfigConstants[] getConfigConstants();
}
