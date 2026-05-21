package store.account.exception;

public class DuplicateAccountEmailException extends RuntimeException {

    public DuplicateAccountEmailException(String email) {
        super("account with email " + email + " already exists");
    }
}
