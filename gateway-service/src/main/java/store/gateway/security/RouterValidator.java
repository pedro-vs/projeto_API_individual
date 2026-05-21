package store.gateway.security;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouterValidator {

    private final List<String> openApiEndpoints = List.of(
        "GET /",
        "GET /health-check",
        "GET /actuator/**",
        "POST /auth/register",
        "GET /auth/logout",
        "POST /auth/login"
    );

    public final Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints
        .stream()
        .noneMatch(route -> {
            String[] parts = route.split(" ", 2);
            String method = parts[0];
            String path = parts[1];
            boolean deep = path.endsWith("/**");
            boolean methodMatches = "ANY".equalsIgnoreCase(method)
                || request.getMethod() != null && request.getMethod().name().equalsIgnoreCase(method);
            boolean pathMatches = request.getURI().getPath().equals(path)
                || deep && request.getURI().getPath().startsWith(path.replace("/**", ""));
            return methodMatches && pathMatches;
        });
}
