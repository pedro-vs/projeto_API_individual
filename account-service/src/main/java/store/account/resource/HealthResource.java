package store.account.resource;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthResource {

    @GetMapping("/")
    public Map<String, String> healthcheck() {
        return Map.of(
            "service", "account-service",
            "status", "ok"
        );
    }
}
