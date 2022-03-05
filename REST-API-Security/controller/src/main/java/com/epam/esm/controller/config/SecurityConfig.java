package com.epam.esm.controller.config;

import com.epam.esm.controller.security.ObjectToJsonMapper;
import com.epam.esm.controller.security.RestAuthenticationEntryPoint;
import com.epam.esm.controller.security.jwt.JwtConfigurer;
import com.epam.esm.controller.security.jwt.JwtTokenFilter;
import com.epam.esm.controller.security.jwt.JwtTokenProvider;
import com.epam.esm.service.dto.RoleDto;
import jdk.nashorn.internal.runtime.UserAccessorProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_ENDPOINT = "/auth/login";
    private static final String ORDERS_ENDPOINT = "/orders";
    private static final String ALL_ENDPOINT = "/";
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectToJsonMapper mapper;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.GET).permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider, mapper));

        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);
    }
}
