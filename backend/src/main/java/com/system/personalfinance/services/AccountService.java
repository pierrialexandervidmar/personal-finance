package com.system.personalfinance.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.personalfinance.dtos.account.CreateAccountRequest;
import com.system.personalfinance.dtos.account.UpdateAccountRequest;
import com.system.personalfinance.entities.Account;
import com.system.personalfinance.entities.User;
import com.system.personalfinance.enums.AccountType;
import com.system.personalfinance.exceptions.ConflictException;
import com.system.personalfinance.repositories.AccountRepository;
import com.system.personalfinance.repositories.UserRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    /** Lista contas do usuário logado. */
    public List<Account> list(Long userId) {
        return accountRepository.findAllByUserIdOrderByIdDesc(userId);
    }

    /** Cria uma conta para o usuário logado. */
    @Transactional
    public Account create(Long userId, CreateAccountRequest req) {
        if (accountRepository.existsByUserIdAndNameIgnoreCase(userId, req.getName())) {
            throw new ConflictException("Já existe uma conta com esse nome");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        AccountType type = req.getType();
        Long balance = req.getInitialBalanceCents() == null ? 0L : req.getInitialBalanceCents();

        Account account = new Account(user, req.getName(), type, balance);
        return accountRepository.save(account);
    }

    /** Busca uma conta do usuário por id. */
    public Account getById(Long userId, Long accountId) {
        return accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
    }

    /** Atualiza uma conta do usuário. */
    @Transactional
    public Account update(Long userId, Long accountId, UpdateAccountRequest req) {
        Account account = getById(userId, accountId);

        // se o nome mudou, checa duplicidade
        if (!account.getName().equalsIgnoreCase(req.getName())
                && accountRepository.existsByUserIdAndNameIgnoreCase(userId, req.getName())) {
            throw new ConflictException("Já existe uma conta com esse nome");
        }

        account.setName(req.getName());
        account.setType(req.getType());

        Long balance = req.getInitialBalanceCents() == null ? 0L : req.getInitialBalanceCents();
        account.setInitialBalanceCents(balance);

        return accountRepository.save(account);
    }

    /** Remove uma conta do usuário. */
    @Transactional
    public void delete(Long userId, Long accountId) {
        Account account = getById(userId, accountId);
        accountRepository.delete(account);
    }
}
