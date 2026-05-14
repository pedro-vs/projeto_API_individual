package store.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDetailOut(
    UUID id,
    LocalDateTime date,
    BigDecimal total,
    String currency,
    List<OrderItemOut> items
) {
}
