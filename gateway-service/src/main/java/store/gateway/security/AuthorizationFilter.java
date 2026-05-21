package store.gateway.security;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import store.gateway.dto.IdentityResponse;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter implements GlobalFilter, Ordered {

    private static final String AUTH_COOKIE_TOKEN = "__store_jwt_token";

    private final RouterValidator routerValidator;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${gateway.auth-solve-url}")
    private String authSolveUrl;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!routerValidator.isSecured.test(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        HttpCookie authCookie = exchange.getRequest().getCookies().getFirst(AUTH_COOKIE_TOKEN);
        if (authCookie == null || authCookie.getValue().isBlank()) {
            return unauthorized(exchange, "Missing authentication cookie");
        }

        String jwt = authCookie.getValue();
        return webClientBuilder.build()
            .post()
            .uri(authSolveUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(Map.of("token", jwt))
            .retrieve()
            .bodyToMono(IdentityResponse.class)
            .flatMap(response -> chain.filter(updateRequest(exchange, response.idAccount(), jwt)))
            .onErrorResume(exception -> unauthorized(exchange, "Invalid token"));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private ServerWebExchange updateRequest(ServerWebExchange exchange, String idAccount, String jwt) {
        return exchange.mutate()
            .request(exchange.getRequest()
                .mutate()
                .header("id-account", idAccount)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .build())
            .build();
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String detail) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] payload = buildPayload(detail);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(payload);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private byte[] buildPayload(String detail) {
        try {
            return objectMapper.writeValueAsBytes(Map.of(
                "title", "Unauthorized request",
                "detail", detail,
                "status", 401
            ));
        } catch (JsonProcessingException exception) {
            return "{\"title\":\"Unauthorized request\",\"status\":401}".getBytes(StandardCharsets.UTF_8);
        }
    }
}
