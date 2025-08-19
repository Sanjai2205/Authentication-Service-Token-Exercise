package com.example.authz_service.service;

import com.example.authz_service.dto.MatchedPermission;
import com.example.authz_service.model.UserPermission;
import com.example.authz_service.repo.UserPermissionRepository;
import com.example.authz_service.util.PatternMatcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PermissionService {

  private final UserPermissionRepository repo;

  public PermissionService(UserPermissionRepository repo) {
    this.repo = repo;
  }

  public DecisionResult decide(String userId, String action, String resource) {
    List<UserPermission> candidates = repo.findByUserAndAction(userId, action);
    List<UserPermission> matched = new ArrayList<>();

    for (UserPermission up : candidates) {
      if (PatternMatcher.matches(up.getResource(), resource)) {
        matched.add(up);
      }
    }

    if (matched.isEmpty()) {
      return DecisionResult.deny(userId, "No matching permissions found");
    }

    // Sort by specificity (desc)
    matched.sort(Comparator.comparingInt((UserPermission p) -> PatternMatcher.specificityScore(p.getResource()))
                           .reversed());

    // Highest specificity bucket
    int bestScore = PatternMatcher.specificityScore(matched.get(0).getResource());
    List<UserPermission> top = matched.stream()
      .filter(p -> PatternMatcher.specificityScore(p.getResource()) == bestScore)
      .toList();

    boolean hasDeny = top.stream().anyMatch(p -> "deny".equalsIgnoreCase(p.getEffect()));
    String decision = hasDeny ? "DENY" : "ALLOW";
    String reason = hasDeny ? "Deny at highest specificity" : "Allow at highest specificity";

    List<MatchedPermission> mp = top.stream()
      .map(p -> new MatchedPermission(p.getAction(), p.getResource(), p.getEffect()))
      .toList();

    return new DecisionResult(decision, userId, reason, mp);
  }

  public static class DecisionResult {
    public final String decision;
    public final String userId;
    public final String reason;
    public final List<MatchedPermission> matches;

    public DecisionResult(String decision, String userId, String reason, List<MatchedPermission> matches) {
      this.decision = decision;
      this.userId = userId;
      this.reason = reason;
      this.matches = matches;
    }

    public static DecisionResult deny(String userId, String reason) {
      return new DecisionResult("DENY", userId, reason, List.of());
    }
  }
}
