package com.quinzex.config;

import com.quinzex.jwtFilter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter Filter){
        this.jwtFilter=Filter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws  Exception{
        security.csrf(AbstractHttpConfigurer::disable).cors(cors->cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth.requestMatchers("/api/internal/academics/hierarchy","/actuator/**","/api/get-exam-categories","/api/registersendotp","/api/questions-random-category","/api/questions-random-chapterid","/api/registeruser","/api/login","/api/login/send-otp","/api/refresh","/api/log-out","/api/contact", "/api/get-papers/bycategory","/api/get-questions","/api/submit-exam", "/api/current-affairs/by-region","/api/get-all-affairs", "/ws/**").permitAll().anyRequest().authenticated())
                .httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable).logout(AbstractHttpConfigurer::disable)    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return security.build();
    }
@Bean
    public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(List.of("https://www.quinzexintelligence.com","https://www.d2sg5wp92ge742.cloudfront.net","https://www.quinzexintelligence.com","https://backend.quinzexintelligence.com"));
      configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
      configuration.setAllowedHeaders(List.of("*"));
      configuration.setExposedHeaders(List.of( "Authorization","Set-Cookie"));
      configuration.setAllowCredentials(true);

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
    }
}
