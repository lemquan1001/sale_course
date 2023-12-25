package com.management_user.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final UserAuthProvider userAuthProvider;
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .addFilterBefore(new JwtAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)//bộ lọc tự tạo ra--> bộ lọc xác thực chính
      .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//bộ lọc cow bản
      .authorizeHttpRequests((requests) -> requests
        .requestMatchers(HttpMethod.POST, "/login","/register").permitAll()
              .requestMatchers("/confirm").permitAll()
        .requestMatchers( "/student/**").authenticated() // Chỉ cho phép người dùng đã đăng nhập
        .requestMatchers("/teacher/**").authenticated()
              .requestMatchers("/course/**").authenticated()
              .requestMatchers("/lesson/**").authenticated()
              .requestMatchers("/comment/**").authenticated()
              .requestMatchers("/package/**").authenticated()
//        .requestMatchers(HttpMethod.GET, "/student/getAll").authenticated()
        .anyRequest().authenticated())
    ;
    return http.build();
  }

}
