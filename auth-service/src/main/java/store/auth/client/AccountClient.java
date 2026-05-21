package store.auth.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import store.auth.client.dto.AccountCreateRequest;
import store.auth.client.dto.AccountCredentialsRequest;
import store.auth.client.dto.AccountResponse;

@FeignClient(name = "account-service", url = "${clients.account-service.url}")
public interface AccountClient {

    @PostMapping("/accounts")
    AccountResponse create(@RequestBody AccountCreateRequest request);

    @GetMapping("/accounts/{id}")
    AccountResponse findById(@PathVariable UUID id);

    @PostMapping("/accounts/search")
    AccountResponse findByEmailAndPassword(@RequestBody AccountCredentialsRequest request);
}
