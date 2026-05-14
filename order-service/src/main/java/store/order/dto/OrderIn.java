package store.order.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record OrderIn(
    @NotEmpty(message = "items must not be empty")
    List<@Valid OrderItemIn> items
) {
}
