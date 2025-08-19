package com.example.authz_service.dto;

import java.util.List;

public class AuthorizeResponse {
  private String decision; // ALLOW or DENY
  private String user_id;
  private String reason;
  private List<MatchedPermission> matched_permissions;

  public AuthorizeResponse(String decision, String user_id, String reason, List<MatchedPermission> matched_permissions) {
    this.decision = decision;
    this.user_id = user_id;
    this.reason = reason;
    this.matched_permissions = matched_permissions;
  }

  public String getDecision() { return decision; }
  public String getUser_id() { return user_id; }
  public String getReason() { return reason; }
  public List<MatchedPermission> getMatched_permissions() { return matched_permissions; }
}
