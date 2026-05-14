package store.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductIn(
    @NotBlank(message = "name is required")
    @Size(max = 255, message = "name must have at most 255 characters")
    String name,

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.01", message = "price must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "price must have up to 2 decimal places")
    BigDecimal price,

    @NotBlank(message = "unit is required")
    @Size(max = 50, message = "unit must have at most 50 characters")
    String unit
) {
}
