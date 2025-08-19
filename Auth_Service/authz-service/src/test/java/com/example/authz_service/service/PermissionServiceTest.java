package com.example.authz_service.service;

import com.example.authz_service.model.UserPermission;
import com.example.authz_service.repo.UserPermissionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PermissionServiceTest {

  @Test
  void specificityAndDenyWins() {
    var repo = Mockito.mock(UserPermissionRepository.class);
    Mockito.when(repo.findByUserAndAction("u", "delete"))
      .thenReturn(List.of(
        new UserPermission(1,"u","delete","transactions","deny"),
        new UserPermission(2,"u","delete","*","allow")
      ));

    var svc = new PermissionService(repo);
    var res = svc.decide("u","delete","transactions/txn-1");
    assertEquals("DENY", res.decision);
  }

  @Test
  void wildcardSegmentMatching() {
    var repo = Mockito.mock(UserPermissionRepository.class);
    Mockito.when(repo.findByUserAndAction("u","write"))
      .thenReturn(List.of(
        new UserPermission(1,"u","write","wallets/*/transactions/*","allow")
      ));
    var svc = new PermissionService(repo);
    var res = svc.decide("u","write","wallets/wallet-456/transactions/txn-999");
    assertEquals("ALLOW", res.decision);
  }
}
