package com.system.personalfinance.resources;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.system.personalfinance.dtos.transaction.CreateTransactionRequest;
import com.system.personalfinance.dtos.transaction.TransactionResponse;
import com.system.personalfinance.dtos.transaction.UpdateTransactionRequest;
import com.system.personalfinance.entities.Transaction;
import com.system.personalfinance.security.UserPrincipal;
import com.system.personalfinance.services.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionResource {

    private final TransactionService transactionService;

    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> list(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal.getId();

        List<TransactionResponse> result = transactionService.list(userId).stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateTransactionRequest request) {

        Long userId = principal.getId();
        Transaction saved = transactionService.create(userId, request);

        URI location = URI.create("/api/transactions/" + saved.getId());
        return ResponseEntity.created(location).body(toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        Transaction t = transactionService.getById(principal.getId(), id);
        return ResponseEntity.ok(toResponse(t));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody UpdateTransactionRequest request) {

        Transaction saved = transactionService.update(principal.getId(), id, request);
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        transactionService.delete(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }

    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getAccount().getId(),
                t.getCategory().getId(),
                t.getType(),
                t.getAmountCents(),
                t.getDescription(),
                t.getOccurredAt(),
                t.getCreatedAt());
    }
}
