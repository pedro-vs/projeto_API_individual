package store.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import store.order.client.dto.ExchangeResponse;
import store.order.config.FeignHeaderPropagationConfig;

@FeignClient(
    name = "exchange-service",
    url = "${clients.exchange-service.url}",
    configuration = FeignHeaderPropagationConfig.class
)
public interface ExchangeClient {

    @GetMapping("/exchanges/{from}/{to}")
    ExchangeResponse getExchange(@PathVariable String from, @PathVariable String to);
}
