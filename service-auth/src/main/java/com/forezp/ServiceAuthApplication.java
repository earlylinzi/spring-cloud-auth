package com.forezp;

import com.forezp.service.security.UserServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@SpringBootApplication
@EnableResourceServer // 开启Resource server
@EnableEurekaClient
public class ServiceAuthApplication {
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	public static void main(String[] args) {
		SpringApplication.run(ServiceAuthApplication.class, args);
	}


	/**
	 * 在任何实现了AuthorizationServerConfigurer接口的类上加上EnableAuthorizationServer注解  开启Authorization Server 功能
	 * 以Bean的形式  注入到Ioc容器中
	 * 需要实现3个配置  也就是重写的3个方法
	 * AuthorizationServerConfigurerAdapter实现了接口AuthorizationServerConfigurer（空实现）
	 *
	 */
	@Configuration
	@EnableAuthorizationServer
	protected  class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

		//private TokenStore tokenStore = new InMemoryTokenStore();// token存在内存中

		JdbcTokenStore tokenStore=new JdbcTokenStore(dataSource);// toekn 存在数据库中

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Autowired
		private UserServiceDetail userServiceDetail;


		/**
		 * ClientDetailsServiceConfigurer  配置客户端信息
		 */
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

			clients.inMemory()
					.withClient("browser")
					.authorizedGrantTypes("refresh_token", "password")
					.scopes("ui")
					.and()
					.withClient("service-hi")
					.secret("123456")
					.authorizedGrantTypes("client_credentials", "refresh_token")
					.scopes("server");

		}

		/**
		 * AuthorizationServerEndpointsConfigurer 配置授权Token的节点和Token服务
		 */
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints
					.tokenStore(tokenStore)//  采用JdbcTokenStore
					.authenticationManager(authenticationManager) // 验证管理
					.userDetailsService(userServiceDetail);// 读取用户信息
		}

		/**
		 * AuthorizationServerSecurityConfigurer  配置token节点的安全策略
		 */
		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			oauthServer
					.tokenKeyAccess("permitAll()")// 获取token的策略  本案例中对获取token接口不拦截
					.checkTokenAccess("isAuthenticated()");// 配置了检查Token策略

		}
	}
}
