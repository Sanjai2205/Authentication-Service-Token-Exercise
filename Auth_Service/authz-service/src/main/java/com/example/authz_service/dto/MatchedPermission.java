package com.example.authz_service.dto;

public class MatchedPermission {
  private String action;
  private String resource;
  private String effect;

  public MatchedPermission() {}

  public MatchedPermission(String action, String resource, String effect) {
    this.action = action;
    this.resource = resource;
    this.effect = effect;
  }

  public String getAction() { return action; }
  public String getResource() { return resource; }
  public String getEffect() { return effect; }
}
