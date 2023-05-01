package com.senzhikong.web;

import com.senzhikong.module.InitializeBean;
import com.senzhikong.spring.SpringContextHolder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author shu
 */
@Component
public class AppInitializeRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, InitializeBean> beanMap = SpringContextHolder.getApplicationContext()
                .getBeansOfType(InitializeBean.class);
        for (InitializeBean bean : beanMap.values()) {
            bean.init();
        }
    }
}
