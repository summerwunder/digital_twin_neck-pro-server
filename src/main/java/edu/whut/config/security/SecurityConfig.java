package edu.whut.config.security;

import edu.whut.config.filter.AuthicationFilter;
import edu.whut.config.security.handler.AuthEntryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SysUserServiceDetail userServiceDetail;
    @Autowired
    private AuthicationFilter authicationFilter;
    @Autowired
    private AuthEntryHandler authEntryHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                auth-> auth.requestMatchers(HttpMethod.POST,"/sys/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/sys/user/register").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .anyRequest().authenticated());
        //禁用session
        http.csrf(AbstractHttpConfigurer::disable);
        //禁用表单
        http.formLogin(AbstractHttpConfigurer::disable);
        http.sessionManagement(AbstractHttpConfigurer::disable);
        //禁用登出页面
        http.logout(AbstractHttpConfigurer::disable);
        //禁用basic保护
        http.httpBasic(AbstractHttpConfigurer::disable);
        //cors跨域允许
        http.cors(Customizer.withDefaults());
        //插入过滤器
        http.addFilterBefore(authicationFilter, UsernamePasswordAuthenticationFilter.class);
        //自定义处理器
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(authEntryHandler) //处理用户未登录（未携带token）
        );
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authenticationProvider
                =new DaoAuthenticationProvider();
        //TODO 实现用户信息
        authenticationProvider.setUserDetailsService(userServiceDetail);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
