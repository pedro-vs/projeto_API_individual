package store.auth.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("email or password is invalid");
    }
}
