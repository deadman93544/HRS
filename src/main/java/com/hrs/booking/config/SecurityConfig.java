package com.hrs.booking.config;

import com.hrs.booking.auth.JwtFilter;
import com.hrs.booking.auth.RateLimitFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public JwtFilter authFilter() {
        return new JwtFilter();
    }


    @Bean
    public RateLimitFilter rateLimitFilter() {
        return new RateLimitFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        All the configurations related to an API call lifecycle before reaching the Controllers is defined here.
//        Configuring Cross-Origin Resource Sharing (CORS) policy, Disabling CSRF (Cross-Site Request Forgery) protection
//        Disabling HTTP Basic authentication, Denying OPTIONS method, Setting session management strategy as STATELESS,
//        Adding Custom Filters - JwtFilter and RateLimitFilter.
        http
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).denyAll()
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/**")
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rateLimitFilter(), JwtFilter.class);
    }
}
