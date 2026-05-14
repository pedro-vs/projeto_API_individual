package store.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderSummaryOut(
    UUID id,
    LocalDateTime date,
    BigDecimal total
) {
}
