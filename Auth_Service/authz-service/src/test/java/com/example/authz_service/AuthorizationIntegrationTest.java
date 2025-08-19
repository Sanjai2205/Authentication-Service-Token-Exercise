package com.example.authz_service;

import com.example.authz_service.config.TestJwtConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestJwtConfig.class)
class AuthorizeIntegrationTest {

  @Autowired
  private TestRestTemplate rest;

  @Test
  void shouldAllowUser123ReadTransactions() {
    String json = """
      {"access_token":"valid_token_for_user123","method":"GET","path":"/transactions"}
    """;
    ResponseEntity<String> resp = post(json);
    assertTrue(resp.getBody().contains("\"decision\":\"ALLOW\""));
  }

  @Test
  void shouldDenyUser123DeleteTransaction() {
    String json = """
      {"access_token":"valid_token_for_user123","method":"DELETE","path":"/transactions/txn-456"}
    """;
    ResponseEntity<String> resp = post(json);
    assertTrue(resp.getBody().contains("\"decision\":\"DENY\""));
  }

  @Test
  void shouldAllowUser456ReadWallet789Txns() {
    String json = """
      {"access_token":"valid_token_for_user456","method":"GET","path":"/wallets/wallet-789/transactions"}
    """;
    ResponseEntity<String> resp = post(json);
    assertTrue(resp.getBody().contains("\"decision\":\"ALLOW\""));
  }

  @Test
  void shouldAllowUser789WriteAnyWalletTxn() {
    String json = """
      {"access_token":"valid_token_for_user789","method":"POST","path":"/wallets/wallet-456/transactions/txn-999"}
    """;
    ResponseEntity<String> resp = post(json);
    assertTrue(resp.getBody().contains("\"decision\":\"ALLOW\""));
  }

  @Test
  void shouldDenyUser456WriteWallet789Txns() {
    String json = """
      {"access_token":"valid_token_for_user456","method":"POST","path":"/wallets/wallet-789/transactions"}
    """;
    ResponseEntity<String> resp = post(json);
    assertTrue(resp.getBody().contains("\"decision\":\"DENY\""));
  }

  @Test
  void shouldAllowAdminWildcard() {
    String json = """
      {"access_token":"valid_token_for_admin789","method":"DELETE","path":"/accounts/acc-123/settings"}
    """;
    ResponseEntity<String> resp = post(json);
    assertTrue(resp.getBody().contains("\"decision\":\"ALLOW\""));
  }

  private ResponseEntity<String> post(String json) {
    HttpHeaders h = new HttpHeaders();
    h.setContentType(MediaType.APPLICATION_JSON);
    return rest.postForEntity("/authorize", new HttpEntity<>(json, h), String.class);
    }
}
