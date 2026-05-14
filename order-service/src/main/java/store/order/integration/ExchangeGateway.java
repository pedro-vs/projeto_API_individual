package store.order.integration;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import store.order.client.ExchangeClient;
import store.order.client.dto.ExchangeResponse;
import store.order.exception.ExternalServiceException;
import store.order.exception.UnsupportedCurrencyException;

@Component
@RequiredArgsConstructor
public class ExchangeGateway {

    private final ExchangeClient exchangeClient;

    public BigDecimal getSellRate(String from, String to) {
        try {
            ExchangeResponse exchange = exchangeClient.getExchange(from, to);
            return exchange.sell();
        } catch (FeignException exception) {
            if (exception.status() == 422) {
                throw new UnsupportedCurrencyException(to);
            }
            throw new ExternalServiceException("Exchange service", "could not convert from " + from + " to " + to);
        }
    }
}
