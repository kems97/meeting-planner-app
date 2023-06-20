package com.meeting.planner.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	Environment env;

	String username;

	String password;
	
	String role;

	@PostConstruct
	private void postConstruct() {
		username = env.getProperty("api.meeting.planner.auth.username");
		password = env.getProperty("api.meeting.planner.auth.password");
		role = env.getProperty("api.meeting.planner.auth.role");
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				.requestMatchers("/api/planner/bestroom", "/api/planner/simulation")
				.authenticated().anyRequest()
				.permitAll())
			.httpBasic(Customizer.withDefaults())
			.csrf((csrf) -> csrf.disable());

		return http.build();
	}

	@Bean
	UserDetailsService userDetailsService() {
		UserDetails user = User.builder()
				.username(username)
				.password("{noop}" + password) // {noop} indique que le mot de passe n'est pas crypté
				.roles(role).build();
		return new InMemoryUserDetailsManager(user);
	}
}
