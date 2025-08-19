package com.example.authz_service.config;

import java.time.Instant;
import java.util.Map;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.*;

@TestConfiguration
public class TestJwtConfig {

  @Bean
  @Primary
  public JwtDecoder testJwtDecoder() {
    return token -> {
      // Map your sample tokens to subjects
      String sub = switch (token) {
        case "valid_token_for_user123" -> "user123";
        case "valid_token_for_user456" -> "user456";
        case "valid_token_for_user789" -> "user789";
        case "valid_token_for_admin789" -> "admin789";
        default -> throw new JwtException("Unknown or invalid token for tests");
      };
      Instant now = Instant.now();
      return new Jwt(
        token, now.minusSeconds(10), now.plusSeconds(3600),
        Map.of("alg","none"),
        Map.of("sub", sub, "aud", "test-audience")
      );
    };
  }
}
