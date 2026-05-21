package store.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenIn(
    @NotBlank(message = "token is required")
    String token
) {
}
