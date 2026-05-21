package store.account.dto;

import java.util.UUID;

public record AccountOut(
    UUID id,
    String name,
    String email
) {
}
