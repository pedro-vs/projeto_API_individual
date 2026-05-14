package store.order;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import store.order.client.dto.ProductResponse;
import store.order.exception.InvalidOrderProductException;
import store.order.exception.UnsupportedCurrencyException;
import store.order.integration.ExchangeGateway;
import store.order.integration.ProductGateway;
import store.order.model.Order;
import store.order.model.OrderItem;
import store.order.repository.OrderRepository;

@SpringBootTest
@AutoConfigureMockMvc
class OrderResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @MockitoBean
    private ProductGateway productGateway;

    @MockitoBean
    private ExchangeGateway exchangeGateway;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    void createOrderReturnsCreatedOrder() throws Exception {
        UUID tomatoId = UUID.randomUUID();
        UUID cheeseId = UUID.randomUUID();

        when(productGateway.getById(tomatoId)).thenReturn(new ProductResponse(tomatoId, "Tomato", new BigDecimal("10.12"), "kg"));
        when(productGateway.getById(cheeseId)).thenReturn(new ProductResponse(cheeseId, "Cheese", new BigDecimal("0.62"), "slice"));

        mockMvc.perform(post("/orders")
                .header("id-account", "70")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "items": [
                        {
                          "idProduct": "%s",
                          "quantity": 2
                        },
                        {
                          "idProduct": "%s",
                          "quantity": 10
                        }
                      ]
                    }
                    """.formatted(tomatoId, cheeseId)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.total").value(26.44))
            .andExpect(jsonPath("$.items.length()").value(2))
            .andExpect(jsonPath("$.items[0].product.id").value(tomatoId.toString()))
            .andExpect(jsonPath("$.items[0].total").value(20.24))
            .andExpect(jsonPath("$.items[1].product.id").value(cheeseId.toString()))
            .andExpect(jsonPath("$.items[1].total").value(6.20));
    }

    @Test
    void findAllReturnsOnlyOrdersFromCurrentAccount() throws Exception {
        UUID firstOrderId = saveOrder("70", LocalDateTime.now().minusHours(1), "26.44", UUID.randomUUID(), 2, "20.24");
        saveOrder("99", LocalDateTime.now().minusHours(2), "11.00", UUID.randomUUID(), 1, "11.00");
        UUID secondOrderId = saveOrder("70", LocalDateTime.now(), "5.00", UUID.randomUUID(), 5, "5.00");

        mockMvc.perform(get("/orders")
                .header("id-account", "70"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(secondOrderId.toString()))
            .andExpect(jsonPath("$[1].id").value(firstOrderId.toString()));
    }

    @Test
    void findByIdReturnsOrderInUsdByDefault() throws Exception {
        UUID productId = UUID.randomUUID();
        UUID orderId = saveOrder("70", LocalDateTime.of(2025, 6, 10, 10, 0), "26.44", productId, 2, "26.44");

        mockMvc.perform(get("/orders/{id}", orderId)
                .header("id-account", "70"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(orderId.toString()))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.total").value(26.44))
            .andExpect(jsonPath("$.items[0].product.id").value(productId.toString()));

        verify(exchangeGateway, never()).getSellRate("USD", "USD");
    }

    @Test
    void findByIdConvertsTotalsWhenCurrencyIsProvided() throws Exception {
        UUID tomatoId = UUID.randomUUID();
        UUID cheeseId = UUID.randomUUID();
        UUID orderId = saveOrderWithTwoItems("70", "26.44", tomatoId, "20.24", 2, cheeseId, "6.20", 10);

        when(exchangeGateway.getSellRate("USD", "BRL")).thenReturn(new BigDecimal("6.00"));

        mockMvc.perform(get("/orders/{id}", orderId)
                .header("id-account", "70")
                .param("currency", "brl"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currency").value("BRL"))
            .andExpect(jsonPath("$.total").value(158.64))
            .andExpect(jsonPath("$.items[0].total").value(121.44))
            .andExpect(jsonPath("$.items[1].total").value(37.20));
    }

    @Test
    void findByIdReturnsNotFoundWhenOrderBelongsToAnotherAccount() throws Exception {
        UUID orderId = saveOrder("99", LocalDateTime.now(), "26.44", UUID.randomUUID(), 2, "26.44");

        mockMvc.perform(get("/orders/{id}", orderId)
                .header("id-account", "70"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.title").value("Order not found"));
    }

    @Test
    void createOrderReturnsBadRequestWhenProductDoesNotExist() throws Exception {
        UUID invalidProductId = UUID.randomUUID();

        when(productGateway.getById(invalidProductId)).thenThrow(new InvalidOrderProductException(invalidProductId));

        mockMvc.perform(post("/orders")
                .header("id-account", "70")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "items": [
                        {
                          "idProduct": "%s",
                          "quantity": 2
                        }
                      ]
                    }
                    """.formatted(invalidProductId)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Invalid order product"));
    }

    @Test
    void findByIdReturnsUnprocessableEntityWhenCurrencyIsUnsupported() throws Exception {
        UUID orderId = saveOrder("70", LocalDateTime.now(), "26.44", UUID.randomUUID(), 2, "26.44");

        when(exchangeGateway.getSellRate("USD", "ABC")).thenThrow(new UnsupportedCurrencyException("ABC"));

        mockMvc.perform(get("/orders/{id}", orderId)
                .header("id-account", "70")
                .param("currency", "ABC"))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.title").value("Unsupported currency"));
    }

    @Test
    void createOrderReturnsUnauthorizedWhenAccountHeaderIsMissing() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "items": [
                        {
                          "idProduct": "%s",
                          "quantity": 2
                        }
                      ]
                    }
                    """.formatted(UUID.randomUUID())))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.title").value("Unauthorized request"));
    }

    private UUID saveOrder(String accountId, LocalDateTime date, String total, UUID productId, int quantity, String itemTotal) {
        Order order = Order.builder()
            .accountId(accountId)
            .date(date)
            .total(new BigDecimal(total))
            .build();

        order.addItem(OrderItem.builder()
            .productId(productId)
            .quantity(quantity)
            .total(new BigDecimal(itemTotal))
            .position(0)
            .build());

        return orderRepository.save(order).getId();
    }

    private UUID saveOrderWithTwoItems(
        String accountId,
        String total,
        UUID firstProductId,
        String firstItemTotal,
        int firstQuantity,
        UUID secondProductId,
        String secondItemTotal,
        int secondQuantity
    ) {
        Order order = Order.builder()
            .accountId(accountId)
            .date(LocalDateTime.now())
            .total(new BigDecimal(total))
            .build();

        order.addItem(OrderItem.builder()
            .productId(firstProductId)
            .quantity(firstQuantity)
            .total(new BigDecimal(firstItemTotal))
            .position(0)
            .build());

        order.addItem(OrderItem.builder()
            .productId(secondProductId)
            .quantity(secondQuantity)
            .total(new BigDecimal(secondItemTotal))
            .position(1)
            .build());

        return orderRepository.save(order).getId();
    }
}
