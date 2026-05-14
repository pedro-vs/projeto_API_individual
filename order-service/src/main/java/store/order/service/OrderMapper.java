package store.order.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import store.order.dto.OrderCreatedOut;
import store.order.dto.OrderDetailOut;
import store.order.dto.OrderItemOut;
import store.order.dto.OrderSummaryOut;
import store.order.dto.ProductRefOut;
import store.order.model.Order;
import store.order.model.OrderItem;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderCreatedOut toCreatedOutput(Order order) {
        return new OrderCreatedOut(
            order.getId(),
            order.getDate(),
            order.getTotal(),
            mapItems(order.getItems(), BigDecimal.ONE)
        );
    }

    public static OrderSummaryOut toSummaryOutput(Order order) {
        return new OrderSummaryOut(
            order.getId(),
            order.getDate(),
            order.getTotal()
        );
    }

    public static OrderDetailOut toDetailOutput(Order order, String currency, BigDecimal rate) {
        return new OrderDetailOut(
            order.getId(),
            order.getDate(),
            convert(order.getTotal(), rate),
            currency,
            mapItems(order.getItems(), rate)
        );
    }

    private static List<OrderItemOut> mapItems(List<OrderItem> items, BigDecimal rate) {
        return items.stream()
            .map(item -> new OrderItemOut(
                new ProductRefOut(item.getProductId()),
                item.getQuantity(),
                convert(item.getTotal(), rate)
            ))
            .toList();
    }

    private static BigDecimal convert(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
