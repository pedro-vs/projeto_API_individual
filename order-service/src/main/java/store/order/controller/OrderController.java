package store.order.controller;

import java.util.List;
import java.util.UUID;

import store.order.dto.OrderCreatedOut;
import store.order.dto.OrderDetailOut;
import store.order.dto.OrderIn;
import store.order.dto.OrderSummaryOut;

public interface OrderController {

    OrderCreatedOut create(String accountId, OrderIn orderIn);

    List<OrderSummaryOut> findAll(String accountId);

    OrderDetailOut findById(String accountId, UUID id, String currency);
}
