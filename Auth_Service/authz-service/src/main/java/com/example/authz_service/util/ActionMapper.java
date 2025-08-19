package com.example.authz_service.util;

public final class ActionMapper {
  private ActionMapper() {}

  public static String toAction(String httpMethod) {
    String m = httpMethod.toUpperCase();
    return switch (m) {
      case "GET", "HEAD", "OPTIONS" -> "read";
      case "POST", "PUT", "PATCH" -> "write";
      case "DELETE" -> "delete";
      default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
    };
  }
}
