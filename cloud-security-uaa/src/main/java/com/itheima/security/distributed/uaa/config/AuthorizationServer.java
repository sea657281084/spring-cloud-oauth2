package com.itheima.security.distributed.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @ClassName AuthorizationServer
 * @Description TODO
 * @Author LHQ
 * @Date 2021/11/19 14:16
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

//    //(1)?????????????????????
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        String secret = new BCryptPasswordEncoder().encode("secret");
//        System.out.println("secret:========>"+secret);
//        clients.inMemory()
//                .withClient("c1")
//                .secret(new BCryptPasswordEncoder().encode("secret"))
//                .resourceIds("res1")
//                .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token") // ???client?????????????????????
//                .scopes("all")
//                .autoApprove(false)  //false ?????????????????????
//                .redirectUris("http://www.baidu.com");
//    }
//    //(2)????????????
//    @Autowired
//    TokenStore tokenStore;
//    @Autowired
//    ClientDetailsService clientDetailsService;
//    @Autowired
//    private JwtAccessTokenConverter accessTokenConverter;
//    @Bean
//    public AuthorizationServerTokenServices tokenServices(){
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setClientDetailsService(clientDetailsService);
//        tokenServices.setSupportRefreshToken(true);
//        tokenServices.setTokenStore(tokenStore);
//
//        //????????????
//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
//        tokenServices.setTokenEnhancer(tokenEnhancerChain);
//
//        tokenServices.setAccessTokenValiditySeconds(7200);  //????????????????????????2??????
//        tokenServices.setRefreshTokenValiditySeconds(259200); //?????????????????????????????????3???
//        return tokenServices;
//    }
//    //(3)????????????????????????
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private AuthorizationCodeServices authorizationCodeServices;
//    @Bean
//    public AuthorizationCodeServices authorizationCodeServices(){
//        return new InMemoryAuthorizationCodeServices();
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.authenticationManager(authenticationManager)
//                .authorizationCodeServices(authorizationCodeServices)
//                .tokenServices(tokenServices())
//                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
//    }
//
//    //(4)???????????????????????????
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.tokenKeyAccess("permitAll()")
//                .checkTokenAccess("permitAll()")
//                .allowFormAuthenticationForClients();
//    }



    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private AuthenticationManager authenticationManager;

    /*** 1.??????????????????????????? */
    @Bean public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //????????????????????????????????????
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /*** 2.??????????????????(token services) */
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service=new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);//?????????????????????
        service.setSupportRefreshToken(true);//??????????????????
        service.setTokenStore(tokenStore);//??????????????????
        //????????????
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);

        service.setAccessTokenValiditySeconds(7200); // ?????????????????????2??????
        service.setRefreshTokenValiditySeconds(259200); // ???????????????????????????3???
        return service;
    }
    /*** 3.???????????????token?????????????????? */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);//?????????????????????????????????????????????
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)//???????????????
                .authorizationCodeServices(authorizationCodeServices)//???????????????
                .tokenServices(tokenService())//??????????????????
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /*** 4.??????????????????(Token Endpoint)??????????????? */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security){
        security
                .tokenKeyAccess("permitAll()")                    //oauth/token_key?????????
                .checkTokenAccess("permitAll()")                  //oauth/check_token??????
                .allowFormAuthenticationForClients()				//??????????????????????????????
        ;
    }
}
