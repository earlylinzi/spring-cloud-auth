package com.forezp.config;

import feign.RequestInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * Created by forezp on 2017/5/31.
 */

@EnableOAuth2Client // 开启 OAUth   Client
@EnableConfigurationProperties
@Configuration
public class OAuth2ClientConfig {

    //        BaseOAuth2ProtectedResourceDetails baseOAuth2ProtectedResourceDetails =  new BaseOAuth2ProtectedResourceDetails();
//        baseOAuth2ProtectedResourceDetails.setClientId("restapp");
//        baseOAuth2ProtectedResourceDetails.setClientSecret("restapp");
//        baseOAuth2ProtectedResourceDetails.setGrantType("password");
//// how to set user name and password ???
//
//        DefaultAccessTokenRequest accessTokenRequest = new DefaultAccessTokenRequest();
//        OAuth2ClientContext oAuth2ClientContext = new DefaultOAuth2ClientContext(accessTokenRequest());
//
//        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(baseOAuth2ProtectedResourceDetails,oAuth2ClientContext);


    /**
     * 该bean是通过读取配置文件中前缀为  "security.oauth2.client" 的配置来获取Bean的属性的
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    /**
     *  @EnableOAuth2Client.
     * 1.oauth2ClientContextFilter
     * 2.AccessTokenRequest
     */
    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(){
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
    }

    /**
     * 向用户uaa服务请求的类型
     * @return
     */
    @Bean
    public OAuth2RestTemplate clientCredentialsRestTemplate() {
        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
    }
}
