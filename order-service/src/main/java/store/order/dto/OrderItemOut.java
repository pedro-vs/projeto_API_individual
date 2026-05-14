package store.order.dto;

import java.math.BigDecimal;

public record OrderItemOut(
    ProductRefOut product,
    int quantity,
    BigDecimal total
) {
}
