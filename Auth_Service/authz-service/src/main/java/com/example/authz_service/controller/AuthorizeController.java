package com.example.authz_service.controller;

import com.example.authz_service.dto.AuthorizeRequest;
import com.example.authz_service.dto.AuthorizeResponse;
import com.example.authz_service.service.PermissionService;
import com.example.authz_service.util.ActionMapper;
import com.example.authz_service.util.ResourceMapper;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthorizeController {

  private static final Logger log = LoggerFactory.getLogger(AuthorizeController.class);

  private final JwtDecoder jwtDecoder;
  private final PermissionService permissionService;

  public AuthorizeController(JwtDecoder jwtDecoder, PermissionService permissionService) {
    this.jwtDecoder = jwtDecoder;
    this.permissionService = permissionService;
  }

  @PostMapping("/authorize")
  public ResponseEntity<?> authorize(@Valid @RequestBody AuthorizeRequest req) {
    String token = req.getAccess_token();
    String action = ActionMapper.toAction(req.getMethod());
    String resource = ResourceMapper.toResource(req.getPath());

    // Validate token + extract identity
    Jwt jwt;
    try {
      jwt = jwtDecoder.decode(token);
    } catch (JwtException e) {
      log.warn("JWT validation failed: {}", e.getMessage());
      return ResponseEntity.badRequest().body(
        new AuthorizeResponse("DENY", null, "Invalid access token: " + e.getMessage(), null)
      );
    }

    String userId = jwt.getClaimAsString("sub");
    if (userId == null || userId.isBlank()) {
      return ResponseEntity.badRequest().body(
        new AuthorizeResponse("DENY", null, "Token missing 'sub' claim for user identity", null)
      );
    }

    var result = permissionService.decide(userId, action, resource);

    String reason = ("ALLOW".equals(result.decision))
      ? "User has " + action + " permission for " + resource
      : result.reason;

    var response = new AuthorizeResponse(result.decision, userId, reason, result.matches);
    log.info("Decision={} user={} method={} path={} resource={} reason={}",
        result.decision, userId, req.getMethod(), req.getPath(), resource, reason);

    return ResponseEntity.ok(response);
  }
}
