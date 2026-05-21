package store.gateway.resource;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayResource {

    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of(
            "service", "gateway-service",
            "status", "ok"
        );
    }

    @GetMapping("/health-check")
    public Map<String, String> healthcheck() {
        return Map.of(
            "service", "gateway-service",
            "status", "ok"
        );
    }
}
