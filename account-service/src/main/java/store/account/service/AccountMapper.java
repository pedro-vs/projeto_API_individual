package store.account.service;

import store.account.dto.AccountOut;
import store.account.model.Account;

public final class AccountMapper {

    private AccountMapper() {
    }

    public static AccountOut toOutput(Account account) {
        return new AccountOut(
            account.getId(),
            account.getName(),
            account.getEmail()
        );
    }
}
