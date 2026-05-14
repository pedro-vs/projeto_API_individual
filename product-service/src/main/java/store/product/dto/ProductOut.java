package store.product.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductOut(
    UUID id,
    String name,
    BigDecimal price,
    String unit
) {
}
