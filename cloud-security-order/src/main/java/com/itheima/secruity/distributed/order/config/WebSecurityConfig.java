package com.itheima.secruity.distributed.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //如果不加这个配置，那么controller中的 “@PreAuthorize("hasAuthority('p1')")//拥有p1权限方可访问此url” 将不起作用
    //安全拦截机制（最重要）
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
//                .antMatchers("/r/r1").hasAuthority("p2")    //注意：这种配置是基于web请求的规则（“@PreAuthorize("hasAuthority('p1')") 这种是基于方法的校验）
//                .antMatchers("/r/r2").hasAuthority("p2")    //注意：这种配置是基于web请求的规则
                .antMatchers("/r/**").authenticated()//所有/r/**的请求必须认证通过
                .anyRequest().permitAll()//除了/r/**，其它的请求可以访问
        ;


    }
}
