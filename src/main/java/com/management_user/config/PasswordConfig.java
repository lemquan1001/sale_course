package com.management_user.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordConfig {
  @Bean
  private PasswordEncoder passwordEncoder(){
    return  new BCryptPasswordEncoder();
  }
}
