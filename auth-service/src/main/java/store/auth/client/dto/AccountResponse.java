package store.auth.client.dto;

import java.util.UUID;

public record AccountResponse(
    UUID id,
    String name,
    String email
) {
}
