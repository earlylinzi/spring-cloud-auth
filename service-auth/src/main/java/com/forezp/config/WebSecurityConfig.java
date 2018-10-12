package com.forezp.config;

import com.forezp.service.security.UserServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by forezp on 2017/5/30.
 */
@Configuration
@EnableWebSecurity // 开启Web保护功能
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启在方法上的保护
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserServiceDetail userServiceDetail;

    /**
     * HttpSecurity 配置了所有请求都需要进行验证
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf().disable();
        // @formatter:on
    }

    /**
     * AuthenticationManagerBuilder配置了验证的用户信息源和密码加密策略
     * 并且在ioc容器中注入了AuthenticationManager对象
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceDetail).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 配置了管理验证的Bean
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
