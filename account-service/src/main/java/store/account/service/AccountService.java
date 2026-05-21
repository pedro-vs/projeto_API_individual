package store.account.service;

import java.util.Locale;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.account.dto.AccountCreateIn;
import store.account.dto.AccountCredentialsIn;
import store.account.dto.AccountOut;
import store.account.exception.AccountNotFoundException;
import store.account.exception.DuplicateAccountEmailException;
import store.account.exception.InvalidCredentialsException;
import store.account.model.Account;
import store.account.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public AccountOut create(AccountCreateIn accountIn) {
        String normalizedEmail = normalizeEmail(accountIn.email());
        if (accountRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new DuplicateAccountEmailException(normalizedEmail);
        }

        Account savedAccount = accountRepository.save(Account.builder()
            .name(accountIn.name().trim())
            .email(normalizedEmail)
            .passwordHash(passwordEncoder.encode(accountIn.password()))
            .build());

        return AccountMapper.toOutput(savedAccount);
    }

    @Transactional(readOnly = true)
    public AccountOut findById(UUID id) {
        return accountRepository.findById(id)
            .map(AccountMapper::toOutput)
            .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public AccountOut findByEmailAndPassword(AccountCredentialsIn credentialsIn) {
        String normalizedEmail = normalizeEmail(credentialsIn.email());
        Account account = accountRepository.findByEmailIgnoreCase(normalizedEmail)
            .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(credentialsIn.password(), account.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return AccountMapper.toOutput(account);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
