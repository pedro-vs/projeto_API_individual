# Order API

O `order-service` e o microservico responsavel por criar pedidos, listar pedidos por conta e consultar detalhes com conversao opcional de moeda.

## Responsabilidades

- criar pedidos para a conta autenticada;
- consultar apenas pedidos do `id-account` recebido;
- validar produtos chamando o `product-service`;
- calcular totais de itens e pedido;
- converter totais usando o `exchange-service`;
- expor metricas e healthcheck.

## Stack

| Item | Tecnologia |
| --- | --- |
| Linguagem | Java 21 |
| Framework | Spring Boot |
| Persistencia | PostgreSQL, JPA e Flyway |
| Integracoes | OpenFeign para Product e Exchange |
| Observabilidade | Actuator e Prometheus |
| Testes | JUnit, MockMvc, H2 e mocks |

## Arquivos principais

- `order-service/src/main/java/store/order/resource/OrderResource.java`
- `order-service/src/main/java/store/order/service/OrderService.java`
- `order-service/src/main/java/store/order/config/FeignHeaderPropagationConfig.java`
- `order-service/src/main/resources/db/migration/V1__create_orders_schema.sql`
- `order-service/src/test/java/store/order/OrderResourceIntegrationTest.java`
