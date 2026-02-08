package com.system.personalfinance.resources;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.personalfinance.dtos.account.AccountResponse;
import com.system.personalfinance.dtos.account.CreateAccountRequest;
import com.system.personalfinance.dtos.account.UpdateAccountRequest;
import com.system.personalfinance.entities.Account;
import com.system.personalfinance.security.UserPrincipal;
import com.system.personalfinance.services.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountResource {

    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> list(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal.getId();

        List<AccountResponse> result = accountService.list(userId).stream()
                .map(a -> new AccountResponse(
                        a.getId(),
                        a.getName(),
                        a.getType(),
                        a.getInitialBalanceCents(),
                        a.getCreatedAt()))
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(
            @Valid @RequestBody CreateAccountRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal.getId();

        Account saved = accountService.create(userId, request);

        AccountResponse body = new AccountResponse(
                saved.getId(),
                saved.getName(),
                saved.getType(),
                saved.getInitialBalanceCents(),
                saved.getCreatedAt());

        URI location = URI.create("/api/accounts/" + saved.getId());
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal.getId();

        Account a = accountService.getById(userId, id);

        AccountResponse body = new AccountResponse(
                a.getId(),
                a.getName(),
                a.getType(),
                a.getInitialBalanceCents(),
                a.getCreatedAt());

        return ResponseEntity.ok(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal.getId();

        Account saved = accountService.update(userId, id, request);

        AccountResponse body = new AccountResponse(
                saved.getId(),
                saved.getName(),
                saved.getType(),
                saved.getInitialBalanceCents(),
                saved.getCreatedAt());

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal.getId();

        accountService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
