package com.senzhikong.cloud.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * @author shu
 */
@Configuration
public class SecuritySecureConfig {

    private final String adminContextPath;

    public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/hello")
                .antMatchers("/", "/hello")
                .antMatchers("/actuator/**")
                .antMatchers("/instances")
                .antMatchers(adminContextPath + "/assets/**")
                //登录页面允许访问
                .antMatchers(adminContextPath + "/login", "/css/**", "/js/**", "/image/*")
                .antMatchers("/instances", "/actuator/**");
    }


    protected void configure(HttpSecurity http) throws Exception {
        // 登录成功处理类
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        http.authorizeRequests()
                //静态文件允许访问
                .antMatchers("/", "/hello").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/instances").permitAll()
                .antMatchers(adminContextPath + "/assets/**").permitAll()
                //登录页面允许访问
                .antMatchers(adminContextPath + "/login", "/css/**", "/js/**", "/image/*").permitAll()
                //其他所有请求需要登录
                .anyRequest().authenticated().and()
                //登录页面配置，用于替换security默认页面
                .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
                //登出页面配置，用于替换security默认页面
                .logout().logoutUrl(adminContextPath + "/logout").and().httpBasic().and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers("/instances", "/actuator/**")
        ;

    }
}
