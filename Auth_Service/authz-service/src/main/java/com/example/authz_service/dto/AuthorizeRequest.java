package com.example.authz_service.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthorizeRequest {
  @NotBlank
  private String access_token;
  @NotBlank
  private String method;
  @NotBlank
  private String path;

  public String getAccess_token() { return access_token; }
  public void setAccess_token(String access_token) { this.access_token = access_token; }
  public String getMethod() { return method; }
  public void setMethod(String method) { this.method = method; }
  public String getPath() { return path; }
  public void setPath(String path) { this.path = path; }
}
