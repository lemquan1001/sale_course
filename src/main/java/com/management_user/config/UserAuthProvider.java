package com.management_user.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.management_user.dtos.UserDto;
import com.management_user.entity.User;
import com.management_user.exceptions.AppException;
import com.management_user.mappers.UserMapper;
import com.management_user.repository.UserRepo;
import com.management_user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {
  private  final UserRepo userRepository;

  private  final UserMapper userMapper;

  @Value("${security.jwt.token.secret-key:secret-key}")

  private String secretKey;

  private final UserService userService;

  @PostConstruct
  protected void init() {
    // this is to avoid having the raw secret key available in the JVM
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(UserDto user) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + 3600000); // 1 hour= 3600000

    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    return JWT.create()
      .withSubject(user.getLogin())
      .withIssuedAt(now)
      .withExpiresAt(validity)
      .withClaim("userName", user.getUsername())
//      .withClaim("lastName", user.getLastname())
      .sign(Algorithm.HMAC256(secretKey));
  }

  public Authentication validateToken(String token) {
    Algorithm algorithm = Algorithm.HMAC256(secretKey);

    JWTVerifier verifier = JWT.require(algorithm)
      .build();

    DecodedJWT decoded = verifier.verify(token);

    UserDto user = UserDto.builder()
      .login(decoded.getSubject())
      .username(decoded.getClaim("userName").asString())
//      .lastname(decoded.getClaim("lastName").asString())
      .build();

    return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
  }

  public Authentication validateTokenStrongly(String token) {
    Algorithm algorithm = Algorithm.HMAC256(secretKey);

    JWTVerifier verifier = JWT.require(algorithm)
      .build();

    DecodedJWT decoded = verifier.verify(token);

    User user = userRepository.findByLogin(decoded.getIssuer())
      .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

    return new UsernamePasswordAuthenticationToken(userMapper.toUserDto(user), null, Collections.emptyList());

//        UserDto user = userService.findByLogin(decoded.getSubject());
//
//        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
  }
}
