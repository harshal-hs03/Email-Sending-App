package com.email.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .antMatcher("/**").authorizeRequests()
//                .antMatchers("/").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login();

        httpSecurity.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2Login();

        // risky to disable this on production
        httpSecurity.csrf().disable();
    }
}
