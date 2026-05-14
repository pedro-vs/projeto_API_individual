package store.order.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import store.order.client.dto.ProductResponse;
import store.order.config.FeignHeaderPropagationConfig;

@FeignClient(
    name = "product-service",
    url = "${clients.product-service.url}",
    configuration = FeignHeaderPropagationConfig.class
)
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductResponse getById(@PathVariable UUID id);
}
