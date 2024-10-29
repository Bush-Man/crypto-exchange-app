package com.apps.trader.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class AppConfig {
    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(requests -> requests
               .requestMatchers("/api/auth/**","/api/home/**")
               .permitAll()
               .anyRequest()
               .authenticated()
          )
          .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
          .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();

    }
    private CorsConfigurationSource corsConfigurationSource(){
        return null;
    }
}
