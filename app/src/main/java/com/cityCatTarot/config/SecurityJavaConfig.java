package com.cityCatTarot.config;

import com.cityCatTarot.application.AuthenticationService;
import com.cityCatTarot.filters.AuthenticationErrorFilter;
import com.cityCatTarot.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import javax.servlet.Filter;

/**
 * Spring Security 설정을 담당합니다.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationService authenticationService;

    public SecurityJavaConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        Filter authenticationFilter = new JwtAuthenticationFilter(
                authenticationManager(), authenticationService);

        Filter authenticationErrorFilter = new AuthenticationErrorFilter();

        http
                .csrf().disable()
                .headers()
                .frameOptions().disable()
                .and()
                .addFilter(authenticationFilter)
                .addFilterBefore(authenticationErrorFilter, JwtAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**").disable();

    }
}
