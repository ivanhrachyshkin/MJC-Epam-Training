package com.epam.esm.controller.config;

import com.epam.esm.controller.security.ObjectToJsonMapper;
import com.epam.esm.controller.security.RestAuthenticationEntryPoint;
import com.epam.esm.controller.security.jwt.JwtTokenFilter;
import com.epam.esm.controller.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_ENDPOINT = "/auth/login";
    private static final String REFRESH_TOKEN_ENDPOINT = "/auth/refreshToken";
    private final ObjectToJsonMapper mapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtTokenFilter authenticationJwtTokenFilter() {
        return new JwtTokenFilter(jwtTokenProvider, mapper);
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
                .antMatchers(REFRESH_TOKEN_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.GET).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);
    }
}
