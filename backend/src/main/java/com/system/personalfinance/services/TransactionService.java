package com.system.personalfinance.services;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.personalfinance.dtos.transaction.CreateTransactionRequest;
import com.system.personalfinance.dtos.transaction.UpdateTransactionRequest;
import com.system.personalfinance.entities.Account;
import com.system.personalfinance.entities.Category;
import com.system.personalfinance.entities.Transaction;
import com.system.personalfinance.entities.User;
import com.system.personalfinance.exceptions.NotFoundException;
import com.system.personalfinance.repositories.AccountRepository;
import com.system.personalfinance.repositories.CategoryRepository;
import com.system.personalfinance.repositories.TransactionRepository;
import com.system.personalfinance.repositories.UserRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            UserRepository userRepository,
            AccountRepository accountRepository,
            CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    /** Lista as transações do usuário (mais recentes primeiro). */
    @Transactional(readOnly = true)
    public List<Transaction> list(Long userId) {
        return transactionRepository.findByUserIdOrderByOccurredAtDesc(userId);
    }

    /** Busca uma transação do usuário por id. */
    @Transactional(readOnly = true)
    public Transaction getById(Long userId, Long id) {
        return transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Transação não encontrada."));
    }

    /**
     * Cria uma transação para o usuário autenticado (valida ownership de account e category).
     */
    @Transactional
    public Transaction create(Long userId, CreateTransactionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        Account account = accountRepository.findByIdAndUserId(request.getAccountId(), userId)
                .orElseThrow(() -> new NotFoundException("Conta não encontrada para este usuário."));

        Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(), userId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada para este usuário."));

        Instant occurredAt = Instant.parse(request.getOccurredAt());

        Transaction t = new Transaction();
        t.setUser(user);
        t.setAccount(account);
        t.setCategory(category);
        t.setType(request.getType());
        t.setAmountCents(request.getAmountCents());
        t.setDescription(request.getDescription());
        t.setOccurredAt(occurredAt);

        return transactionRepository.save(t);
    }

    /** Atualiza uma transação do usuário. */
    @Transactional
    public Transaction update(Long userId, Long id, UpdateTransactionRequest request) {
        Transaction t = getById(userId, id);

        Account account = accountRepository.findByIdAndUserId(request.getAccountId(), userId)
                .orElseThrow(() -> new NotFoundException("Conta não encontrada para este usuário."));

        Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(), userId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada para este usuário."));

        Instant occurredAt = Instant.parse(request.getOccurredAt());

        t.setAccount(account);
        t.setCategory(category);
        t.setType(request.getType());
        t.setAmountCents(request.getAmountCents());
        t.setDescription(request.getDescription());
        t.setOccurredAt(occurredAt);

        return transactionRepository.save(t);
    }

    /** Remove uma transação do usuário. */
    @Transactional
    public void delete(Long userId, Long id) {
        Transaction t = getById(userId, id);
        transactionRepository.delete(t);
    }
}
