package store.gateway;

import static org.springframework.test.web.reactive.server.WebTestClient.bindToServer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "clients.account-service.url=http://localhost:18083",
    "clients.auth-service.url=http://localhost:18084",
    "clients.product-service.url=http://localhost:18080",
    "clients.order-service.url=http://localhost:18081",
    "clients.exchange-service.url=http://localhost:18000",
    "gateway.auth-solve-url=http://localhost:18084/auth/solve"
})
class GatewayResourceIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    void rootHealthcheckIsOpen() {
        WebTestClient client = bindToServer().baseUrl("http://localhost:" + port).build();

        client.get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.service").isEqualTo("gateway-service")
            .jsonPath("$.status").isEqualTo("ok");
    }

    @Test
    void securedRouteWithoutCookieReturnsUnauthorized() {
        WebTestClient client = bindToServer().baseUrl("http://localhost:" + port).build();

        client.get()
            .uri("/products")
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody()
            .jsonPath("$.title").isEqualTo("Unauthorized request");
    }
}
