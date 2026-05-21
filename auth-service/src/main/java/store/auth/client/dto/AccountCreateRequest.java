package store.auth.client.dto;

public record AccountCreateRequest(
    String name,
    String email,
    String password
) {
}
