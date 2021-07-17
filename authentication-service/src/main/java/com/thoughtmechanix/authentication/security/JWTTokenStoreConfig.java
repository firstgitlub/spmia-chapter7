package com.thoughtmechanix.authentication.security;

import com.thoughtmechanix.authentication.config.ServiceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;

@Configuration
public class JWTTokenStoreConfig {

    @Resource
    private ServiceConfig serviceConfig;

    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     *  用于从出示给服务的令牌中 读取数据
     * @return
     */
    @Bean
    @Primary //注解告诉spring,如果有多个特定类型的bean,那么就使用被@Primary标注的bean类型进行自动注入
    public DefaultTokenServices tokenServices(){
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        // 在JWT 和 OAuth2服务器之间 充当翻译
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        /**
         * 使用SpringCloud Security 支持对称秘钥加密 和 使用公钥/私钥的不对称加密
         */
        // 定义将用于签署 令牌的 签名秘钥
        converter.setSigningKey(serviceConfig.getJwtSigningKey());
        return converter;
    }

    @Bean
    public TokenEnhancer jwtTokenEnhancer(){
        return new TokenEnhancerChain();
    }

}
