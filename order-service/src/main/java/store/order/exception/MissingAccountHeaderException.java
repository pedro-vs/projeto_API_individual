package store.order.exception;

public class MissingAccountHeaderException extends RuntimeException {

    public MissingAccountHeaderException() {
        super("header id-account is required");
    }
}
