package com.example.authz_service.model;

public class UserPermission {
  private final long id;
  private final String userId;
  private final String action;
  private final String resource;
  private final String effect; // allow | deny

  public UserPermission(long id, String userId, String action, String resource, String effect) {
    this.id = id;
    this.userId = userId;
    this.action = action;
    this.resource = resource;
    this.effect = effect;
  }

  public long getId() { return id; }
  public String getUserId() { return userId; }
  public String getAction() { return action; }
  public String getResource() { return resource; }
  public String getEffect() { return effect; }
}
