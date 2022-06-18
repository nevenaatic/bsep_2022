package com.example.demo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.demo.service.CustomUserDetailsService;

import com.example.demo.security.RestAuthenticationEntryPoint;
import com.example.demo.security.TokenAuthenticationFilter;
import com.example.demo.utils.TokenUtils;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        
    }

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
                .authorizeRequests()
                
                .antMatchers("/appUser/getAllUsers").permitAll()
                
                .antMatchers("/registration/registerUser").permitAll()
                .antMatchers("/registration/login").permitAll()
                .antMatchers("/registration/emailVerification").permitAll()
                .antMatchers("/registration/passwordlessLogin").permitAll()
                .antMatchers("/registration/checkEmail").permitAll()
                .antMatchers("/registration/changePassword").permitAll()
                .antMatchers("/registration/checkEmailPassChange").permitAll()

                .antMatchers("/certificate/getAllCertificates").permitAll()
                .antMatchers("/certificate/getCertificatesById/**").permitAll()
                .antMatchers("/certificate/getIssuedCertificatesById/**").permitAll()
                .antMatchers("/certificate/getAllCertificates").permitAll()
                .antMatchers("/certificate/createCertificate").hasAuthority("PERM_CERT_ISSUE")
                .antMatchers("/certificate/revokeCertificate").hasAuthority("PERM_CERT_REVOKE")
                .antMatchers("/certificate/checkValidity/**").hasAuthority("PERM_CERT_CHECK_VALIDITY")
                .antMatchers("/certificate/downloadCertificate").hasAuthority("PERM_CERT_DOWNLOAD")
                 
                
                .anyRequest().authenticated().and()
                .cors().and()
                .addFilterBefore(new TokenAuthenticationFilter(tokenUtils, customUserDetailsService), BasicAuthenticationFilter.class);

        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico", "/**/*.html",
                "/**/*.css", "/**/*.js");
    }

}
