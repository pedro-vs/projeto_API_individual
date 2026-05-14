package store.order.client.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExchangeResponse(
    BigDecimal sell,
    BigDecimal buy,
    String date,
    @JsonProperty("id-account")
    String idAccount
) {
}
