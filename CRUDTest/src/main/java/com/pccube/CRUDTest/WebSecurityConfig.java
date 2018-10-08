package com.pccube.CRUDTest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceIMP userDetailsServiceIMP;
	
	@Autowired
	private SimpleAuthenticationSuccessHandler successHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/homeAdmin").hasAuthority("admin")
		.antMatchers("/homeUser").hasAuthority("user").anyRequest().authenticated().and().formLogin().successHandler(successHandler)
				.loginPage("/login").usernameParameter("username").passwordParameter("password")

				.permitAll().and().logout().permitAll();
		
		
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}

	@Override
	public void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsServiceIMP)

				.passwordEncoder(passwordEncoder());

	}

	@Bean
	public UserDetailsServiceIMP UserDetailsService() {
		return new UserDetailsServiceIMP();
	};

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}