package com.senzhikong.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * @author Shu.zhou
 * @date 2019年1月16日下午4:07:56
 */
public class SpringBeanNameGenerator implements BeanNameGenerator {
 

    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return definition.getBeanClassName();
    }

}
