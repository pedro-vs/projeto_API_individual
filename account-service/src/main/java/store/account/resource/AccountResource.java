package store.account.resource;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.account.dto.AccountCreateIn;
import store.account.dto.AccountCredentialsIn;
import store.account.dto.AccountOut;
import store.account.service.AccountService;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountResource {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountOut create(@Valid @RequestBody AccountCreateIn accountIn) {
        return accountService.create(accountIn);
    }

    @GetMapping("/{id}")
    public AccountOut findById(@PathVariable UUID id) {
        return accountService.findById(id);
    }

    @PostMapping("/search")
    public AccountOut findByEmailAndPassword(@Valid @RequestBody AccountCredentialsIn credentialsIn) {
        return accountService.findByEmailAndPassword(credentialsIn);
    }
}
