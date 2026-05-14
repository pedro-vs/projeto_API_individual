package store.order.resource;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.order.controller.OrderController;
import store.order.dto.OrderCreatedOut;
import store.order.dto.OrderDetailOut;
import store.order.dto.OrderIn;
import store.order.dto.OrderSummaryOut;
import store.order.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderResource implements OrderController {

    private final OrderService orderService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderCreatedOut create(@RequestHeader("id-account") String accountId, @Valid @RequestBody OrderIn orderIn) {
        return orderService.create(accountId, orderIn);
    }

    @Override
    @GetMapping
    public List<OrderSummaryOut> findAll(@RequestHeader("id-account") String accountId) {
        return orderService.findAll(accountId);
    }

    @Override
    @GetMapping("/{id}")
    public OrderDetailOut findById(
        @RequestHeader("id-account") String accountId,
        @PathVariable UUID id,
        @RequestParam(required = false) String currency
    ) {
        return orderService.findById(accountId, id, currency);
    }
}
