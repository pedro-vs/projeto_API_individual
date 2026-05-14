package store.order.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.order.client.dto.ProductResponse;
import store.order.dto.OrderCreatedOut;
import store.order.dto.OrderDetailOut;
import store.order.dto.OrderIn;
import store.order.dto.OrderSummaryOut;
import store.order.exception.MissingAccountHeaderException;
import store.order.exception.OrderNotFoundException;
import store.order.integration.ExchangeGateway;
import store.order.integration.ProductGateway;
import store.order.model.Order;
import store.order.model.OrderItem;
import store.order.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String DEFAULT_CURRENCY = "USD";

    private final OrderRepository orderRepository;
    private final ProductGateway productGateway;
    private final ExchangeGateway exchangeGateway;

    @Transactional
    public OrderCreatedOut create(String accountId, OrderIn orderIn) {
        String normalizedAccountId = normalizeAccountId(accountId);

        Order order = Order.builder()
            .accountId(normalizedAccountId)
            .date(LocalDateTime.now())
            .total(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
            .build();

        BigDecimal total = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        for (int index = 0; index < orderIn.items().size(); index++) {
            ProductResponse product = productGateway.getById(orderIn.items().get(index).idProduct());
            BigDecimal itemTotal = product.price()
                .multiply(BigDecimal.valueOf(orderIn.items().get(index).quantity()))
                .setScale(2, RoundingMode.HALF_UP);

            OrderItem item = OrderItem.builder()
                .productId(product.id())
                .quantity(orderIn.items().get(index).quantity())
                .total(itemTotal)
                .position(index)
                .build();

            order.addItem(item);
            total = total.add(itemTotal);
        }

        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toCreatedOutput(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderSummaryOut> findAll(String accountId) {
        String normalizedAccountId = normalizeAccountId(accountId);
        return orderRepository.findAllByAccountIdOrderByDateDesc(normalizedAccountId)
            .stream()
            .map(OrderMapper::toSummaryOutput)
            .toList();
    }

    @Transactional(readOnly = true)
    public OrderDetailOut findById(String accountId, UUID id, String currency) {
        String normalizedAccountId = normalizeAccountId(accountId);
        Order order = orderRepository.findByIdAndAccountId(id, normalizedAccountId)
            .orElseThrow(() -> new OrderNotFoundException(id));

        String normalizedCurrency = normalizeCurrency(currency);
        BigDecimal rate = DEFAULT_CURRENCY.equals(normalizedCurrency)
            ? BigDecimal.ONE
            : exchangeGateway.getSellRate(DEFAULT_CURRENCY, normalizedCurrency);

        return OrderMapper.toDetailOutput(order, normalizedCurrency, rate);
    }

    private String normalizeAccountId(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new MissingAccountHeaderException();
        }
        return accountId.trim();
    }

    private String normalizeCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            return DEFAULT_CURRENCY;
        }
        return currency.trim().toUpperCase(Locale.ROOT);
    }
}
