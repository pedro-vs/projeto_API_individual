package store.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderCreatedOut(
    UUID id,
    LocalDateTime date,
    BigDecimal total,
    List<OrderItemOut> items
) {
}
