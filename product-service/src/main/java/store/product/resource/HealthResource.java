package store.product.resource;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthResource {

    @GetMapping("/")
    public Map<String, String> healthcheck() {
        return Map.of(
            "service", "product-service",
            "status", "ok"
        );
    }
}
