package com.example.authz_service.util;

public final class ResourceMapper {
  private ResourceMapper() {}

  /** "/wallets/wallet-789/transactions" -> "wallets/wallet-789/transactions" */
  public static String toResource(String path) {
    if (path == null || path.isBlank()) return "";
    String p = path.trim();
    while (p.startsWith("/")) p = p.substring(1);
    if (p.endsWith("/")) p = p.substring(0, p.length() - 1);
    return p;
  }
}
