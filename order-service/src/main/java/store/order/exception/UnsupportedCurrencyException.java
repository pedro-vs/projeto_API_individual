package store.order.exception;

public class UnsupportedCurrencyException extends RuntimeException {

    public UnsupportedCurrencyException(String currency) {
        super("currency " + currency + " is not supported");
    }
}
