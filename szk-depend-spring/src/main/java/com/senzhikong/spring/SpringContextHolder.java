package com.senzhikong.spring;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author shu
 */
@Slf4j
@Component
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext = null;

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    /**
     * 实现ApplicationContextAware接口, 注入Context到静态变量中.
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        if (SpringContextHolder.applicationContext != null) {
            log.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:{}", SpringContextHolder.applicationContext);
        }
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBeanByClassName(String className) {
        assertContextInjected();
        Class<T> clz;
        try {
            clz = (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return applicationContext.getBean(clz);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        try {
            return applicationContext.getBean(requiredType);
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    public static void clearHolder() {
        log.debug("清除SpringContextHolder中的ApplicationContext:{}", applicationContext);
        applicationContext = null;
    }

    /**
     * 检查ApplicationContext不为空.
     */
    private static void assertContextInjected() {
        Validate.validState(applicationContext != null,
                "applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
    }

    /**
     * 实现DisposableBean接口, 在Context关闭时清理静态变量.
     */
    @Override
    public void destroy() {
        SpringContextHolder.clearHolder();
    }

    /**
     * 主动向Spring容器中注册bean
     *
     * @param name  BeanName
     * @param clazz 注册的bean的类性
     * @param args  构造方法的必要参数，顺序和类型要求和clazz中定义的一致
     * @return 返回注册到容器中的bean对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T registerBean(String name, Class<T> clazz,
                                     Object... args) {
        if (containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {

                return (T) bean;
            } else {
                throw new RuntimeException("BeanName 重复 " + name);
            }
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }

        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);

        return applicationContext.getBean(name, clazz);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static void removeBean(String name) {
        if (!containsBean(name)) {
            return;
        }
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
        beanFactory.removeBeanDefinition(name);
    }
}
