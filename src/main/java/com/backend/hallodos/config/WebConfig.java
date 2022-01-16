package com.backend.hallodos.config;


import com.backend.hallodos.services.UserServiceHallo;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter {

    //UntukRegister
    @Bean
    public PasswordEncoder bCryptPasswoardEncoder() {
    return new BCryptPasswordEncoder();
    }


    //bagian untuk login user anotasi @bean@Override
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserServiceHallo();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService());
        return authProvider;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth)throws Exception{
    auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers("/register","/login","/dasboard").permitAll()
        .antMatchers("/css/**","/font/**","/imgage/**","/js/**").permitAll()
        .antMatchers("/mahasiswa").hasAnyAuthority("MAHASISWA")
        .antMatchers("/dosen").hasAnyAuthority("DOSEN")
        .anyRequest().authenticated().and()
        .formLogin().defaultSuccessUrl("/dasboard").permitAll()
        .and()
        .logout().permitAll();

        // super.configure(http);
    }
 
   
    
}
