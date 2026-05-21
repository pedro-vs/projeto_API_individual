package store.auth.client.dto;

public record AccountCredentialsRequest(
    String email,
    String password
) {
}
