package store.auth.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("token is invalid or expired");
    }
}
