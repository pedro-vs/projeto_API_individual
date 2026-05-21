package store.auth.exception;

public class MissingAccountHeaderException extends RuntimeException {

    public MissingAccountHeaderException() {
        super("Required request header 'id-account' is not present");
    }
}
