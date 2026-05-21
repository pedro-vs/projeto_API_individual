package store.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import store.auth.client.AccountClient;
import store.auth.client.dto.AccountCreateRequest;
import store.auth.client.dto.AccountCredentialsRequest;
import store.auth.client.dto.AccountResponse;
import store.auth.dto.RegisterIn;
import store.auth.dto.TokenOut;
import store.auth.dto.WhoAmIOut;
import store.auth.exception.DuplicateAccountException;
import store.auth.exception.ExternalServiceException;
import store.auth.exception.InvalidCredentialsException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountClient accountClient;
    private final JwtService jwtService;

    public void register(RegisterIn registerIn) {
        try {
            accountClient.create(new AccountCreateRequest(
                registerIn.name(),
                registerIn.email(),
                registerIn.password()
            ));
        } catch (FeignException.Conflict exception) {
            throw new DuplicateAccountException(registerIn.email());
        } catch (FeignException exception) {
            throw new ExternalServiceException("account-service request failed");
        }
    }

    public TokenOut login(String email, String password) {
        try {
            AccountResponse account = accountClient.findByEmailAndPassword(new AccountCredentialsRequest(email, password));
            return new TokenOut(jwtService.generate(account));
        } catch (FeignException.Unauthorized exception) {
            throw new InvalidCredentialsException();
        } catch (FeignException exception) {
            throw new ExternalServiceException("account-service request failed");
        }
    }

    public String solveToken(String token) {
        return jwtService.getId(token).toString();
    }

    public WhoAmIOut whoAmI(String idAccount) {
        try {
            AccountResponse account = accountClient.findById(UUID.fromString(idAccount.trim()));
            return new WhoAmIOut(
                account.id().toString(),
                account.name(),
                account.email()
            );
        } catch (IllegalArgumentException exception) {
            throw new InvalidCredentialsException();
        } catch (FeignException exception) {
            throw new ExternalServiceException("account-service request failed");
        }
    }
}
