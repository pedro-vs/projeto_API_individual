package store.auth.exception;

public class DuplicateAccountException extends RuntimeException {

    public DuplicateAccountException(String email) {
        super("account with email " + email + " already exists");
    }
}
