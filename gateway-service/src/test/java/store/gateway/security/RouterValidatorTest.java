package store.gateway.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

class RouterValidatorTest {

    private final RouterValidator routerValidator = new RouterValidator();

    @Test
    void loginRouteIsOpen() {
        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.POST, "/auth/login").build();
        assertThat(routerValidator.isSecured.test(request)).isFalse();
    }

    @Test
    void productRouteIsSecured() {
        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.GET, "/products").build();
        assertThat(routerValidator.isSecured.test(request)).isTrue();
    }

    @Test
    void actuatorRouteIsOpen() {
        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.GET, "/actuator/prometheus").build();
        assertThat(routerValidator.isSecured.test(request)).isFalse();
    }
}
