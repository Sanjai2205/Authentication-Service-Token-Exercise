package com.example.authz_service.util;

import java.util.regex.Pattern;

/**
 * Wildcard rules:
 * - "*" as a whole pattern matches everything.
 * - "*" inside a segment matches exactly one segment ([^/]+).
 * - Full-string match (pattern must cover the entire resource).
 */
public final class PatternMatcher {
  private PatternMatcher() {}

  public static boolean matches(String pattern, String resource) {
    if (pattern.equals("*")) return true;
    String regex = convert(pattern);
    return Pattern.matches(regex, resource);
  }

  // Specificity score: more literal chars, fewer '*' -> higher score
  public static int specificityScore(String pattern) {
    if (pattern.equals("*")) return -1; // least specific
    int total = pattern.length();
    long stars = pattern.chars().filter(ch -> ch == '*').count();
    return (int)(total - stars * 10); // weight fewer '*' much higher
  }

  private static String convert(String pattern) {
    StringBuilder sb = new StringBuilder();
    sb.append("^");
    for (int i = 0; i < pattern.length(); i++) {
      char c = pattern.charAt(i);
      if (c == '*') {
        sb.append("[^/]+");
      } else {
        // escape regex specials except '/'
        if ("\\.[]{}()+-^$|?".indexOf(c) >= 0) sb.append("\\");
        sb.append(c);
      }
    }
    sb.append("$");
    return sb.toString();
  }
}
