package store.auth.dto;

public record WhoAmIOut(
    String id,
    String name,
    String email
) {
}
