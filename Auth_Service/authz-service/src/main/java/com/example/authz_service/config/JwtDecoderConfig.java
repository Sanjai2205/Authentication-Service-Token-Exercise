package com.example.authz_service.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.oauth2.jwt.*;

@Configuration
public class JwtDecoderConfig {

  @Value("${auth.issuer:}")
  private String issuer;

  @Value("${auth.audience:}")
  private String audience;

  /**
   * If ISSUER is provided, configure NimbusJwtDecoder to validate signature, exp, and audience.
   * If not provided, no decoder bean is created here (tests will supply a mock decoder via @TestConfiguration).
   */
  @Bean
  public JwtDecoder jwtDecoder() {
    if (issuer == null || issuer.isBlank()) {
      // In dev without provider, you can comment this out and run test profile instead.
      throw new IllegalStateException("No ISSUER configured. Set ISSUER env var for real JWT validation, or run tests.");
    }
    NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuer);
    OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
    OAuth2TokenValidator<Jwt> withAudience = new JwtClaimValidator<List<String>>("aud",
        aud -> aud != null && aud.contains(audience));
    decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));
    return decoder;
  }
}
