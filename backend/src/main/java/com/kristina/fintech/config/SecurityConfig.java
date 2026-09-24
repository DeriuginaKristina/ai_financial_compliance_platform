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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            // CORS is handled by Spring Security
            .cors(cors -> {})

            // Stateless API using Authorization headers, not cookies
            .csrf(csrf -> csrf.disable())

            .sessionManagement(s ->
                    s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(a -> a
                    // Public health endpoints
                    .requestMatchers("/actuator/health", "/actuator/info").permitAll()

                    // Current demo authorization
                    .requestMatchers("/api/**").hasRole("ACCOUNTANT")

                    // Everything else requires authentication
                    .anyRequest().authenticated()
            )

            .httpBasic(h -> {});

    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // Change this to your frontend URL
    configuration.setAllowedOrigins(
            List.of("http://localhost:3000")
    );

    configuration.setAllowedMethods(
            List.of(
                    "GET",
                    "POST",
                    "PUT",
                    "DELETE",
                    "OPTIONS"
            )
    );

    configuration.setAllowedHeaders(
            List.of(
                    "Content-Type",
                    "Authorization",
                    "Accept",
                    "Origin"
            )
    );

    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", configuration);

    return source;
  }

  @Bean
  UserDetailsService users(
          @Value("${app.security.username}") String username,
          @Value("${app.security.password}") String password,
          PasswordEncoder encoder) {

    return new InMemoryUserDetailsManager(
            User.withUsername(username)
                    .password(encoder.encode(password))
                    .roles("ACCOUNTANT")
                    .build()
    );
  }
}
