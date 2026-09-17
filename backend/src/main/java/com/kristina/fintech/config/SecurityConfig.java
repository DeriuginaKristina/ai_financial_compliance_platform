package com.kristina.fintech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean PasswordEncoder passwordEncoder() { return PasswordEncoderFactories.createDelegatingPasswordEncoder(); }

  @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      // Stateless API: CSRF protection is disabled because the demo uses Authorization headers, not cookies.
      // Production deployments should use OIDC/OAuth2 and a browser-safe CSRF strategy where applicable.
      .csrf(csrf -> csrf.disable())
      .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(a -> a
        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
        .requestMatchers("/api/**").hasRole("ACCOUNTANT")
        .anyRequest().authenticated())
      .httpBasic(h -> {});
    return http.build();
  }

  @Bean UserDetailsService users(@Value("${app.security.username}") String username,
                                 @Value("${app.security.password}") String password,
                                 PasswordEncoder encoder) {
    return new InMemoryUserDetailsManager(User.withUsername(username)
      .password(encoder.encode(password)).roles("ACCOUNTANT").build());
  }
}
