package store.order.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemIn(
    @NotNull(message = "idProduct is required")
    UUID idProduct,

    @Min(value = 1, message = "quantity must be greater than zero")
    int quantity
) {
}
