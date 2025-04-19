package com.senior.care.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.senior.care.filter.JwtAuthFilter;

//import com.javatechie.config.UserInfoUserDetailsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	List<String> URLLIST = new ArrayList<>(Arrays.asList("/users/authenticate", "/users/new", "/users/welcome"));
	RequestMatcher[] requestMatchers = URLLIST.stream()
            .map(AntPathRequestMatcher::new)
            .toArray(RequestMatcher[]::new);
//	 @Autowired
//	    private CorsConfigurationSimple corsConfigurationSimple;
	 @Autowired
	    private JwtAuthFilter authFilter;
	@Bean
	public UserDetailsService userDetailsService()
	{
	        return new UserInfoUserDetailsService();
		    }
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    return http.csrf().disable()
	        .authorizeHttpRequests()
	        .requestMatchers(requestMatchers).permitAll() // Permit all for specific endpoints
	        .and() // <- This is not necessary
	        .authorizeHttpRequests().requestMatchers("/users/**") // This line is redundant
	        .authenticated()
	        .and()
	        .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        .and()
	        .authenticationProvider(authenticationProvider())
	        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
	        .build();
	}


	
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    @Bean
	     public AuthenticationProvider authenticationProvider(){
	         DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
	         authenticationProvider.setUserDetailsService(userDetailsService());
	         authenticationProvider.setPasswordEncoder(passwordEncoder());
	         return authenticationProvider;
	     }
	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	    }
	    
//	    public void configure(WebSecurity web)
//	               throws Exception {
//	     web.ignoring().requestMatchers(HttpMethod.POST, "/users/authenticate");
//	}

}
