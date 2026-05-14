package store.order.integration;

import java.util.UUID;

import org.springframework.stereotype.Component;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import store.order.client.ProductClient;
import store.order.client.dto.ProductResponse;
import store.order.exception.ExternalServiceException;
import store.order.exception.InvalidOrderProductException;

@Component
@RequiredArgsConstructor
public class ProductGateway {

    private final ProductClient productClient;

    public ProductResponse getById(UUID id) {
        try {
            return productClient.getById(id);
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new InvalidOrderProductException(id);
            }
            throw new ExternalServiceException("Product service", "could not load product " + id);
        }
    }
}
