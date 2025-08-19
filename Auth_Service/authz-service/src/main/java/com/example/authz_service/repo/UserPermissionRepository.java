package com.example.authz_service.repo;

import com.example.authz_service.model.UserPermission;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserPermissionRepository {

  private final JdbcTemplate jdbc;

  public UserPermissionRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<UserPermission> findByUserAndAction(String userId, String action) {
    return jdbc.query(
      "SELECT id, user_id, action, resource, effect FROM user_permissions WHERE user_id = ? AND action = ?",
      (rs, rowNum) -> map(rs), userId, action
    );
  }

  public List<UserPermission> findByUser(String userId) {
    return jdbc.query(
      "SELECT id, user_id, action, resource, effect FROM user_permissions WHERE user_id = ?",
      (rs, rowNum) -> map(rs), userId
    );
  }

  private static UserPermission map(ResultSet rs) throws SQLException {
    return new UserPermission(
      rs.getLong("id"),
      rs.getString("user_id"),
      rs.getString("action"),
      rs.getString("resource"),
      rs.getString("effect")
    );
  }
}
