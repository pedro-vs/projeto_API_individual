package store.account.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(UUID id) {
        super("account with id " + id + " was not found");
    }
}
