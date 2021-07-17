package com.thoughtmechanix.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
public class OAuth2Config extends
    // 继承AuthorizationServerConfigurerAdapter 类，并使用@Configuration 注解 标注这个类
    AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     *  验证是用户通过 提供凭证来证明 他们是谁的行为
     *  授权决定是否允许用户做他们想做的事情
     */
    // 覆盖configure() 方法，这定义了 哪些客户端将注册到  认证服务
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 针对于应用程序的信息 支持的存储方式：内存存储 和 JDBC存储
        clients.inMemory()
            // 注册了一个 名为 eagleeye的应用程序
                .withClient("eagleeye")
            // 这个密码用于 调用发起服务 获取认证token 是，传输到 认证服务时要用的
                .secret("thisissecret")
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                .scopes("webclient", "mobileclient");
    }

    // 该方法定义了 AuthenticationServerConfigurer 中使用的不通组件
    // 这段代码告诉 Spring 使用Spring 提供的默认验证管理器 和 用户详细信息服务
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
      endpoints
        .authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService);
    }
}
