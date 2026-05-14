package store.order.exception;

import java.util.UUID;

public class InvalidOrderProductException extends RuntimeException {

    public InvalidOrderProductException(UUID productId) {
        super("product with id " + productId + " cannot be used in this order");
    }
}
