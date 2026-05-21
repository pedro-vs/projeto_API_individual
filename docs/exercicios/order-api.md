# Order API

A Order API implementa criacao e consulta de pedidos vinculados a uma conta autenticada.

## Endpoints

- `POST /orders`: cria pedido para o usuario informado no header `id-account`.
- `GET /orders`: lista pedidos do usuario.
- `GET /orders/{id}`: consulta detalhes do pedido.
- `GET /orders/{id}?currency=BRL`: converte totais usando Exchange API.
- `GET /`: healthcheck simples.
- `GET /actuator/prometheus`: metricas para Prometheus.

## Implementacao

- Spring Boot com Java 21.
- OpenFeign para consumir Product API e Exchange API.
- PostgreSQL em runtime.
- Flyway para criacao do schema `orders`.
- H2 e mocks em testes automatizados.
- Tratamento de erros para produto inexistente, pedido inexistente, moeda invalida e falhas externas.

## Arquivos principais

- `order-service/src/main/java/store/order/resource/OrderResource.java`
- `order-service/src/main/java/store/order/service/OrderService.java`
- `order-service/src/main/java/store/order/integration/ProductGateway.java`
- `order-service/src/main/java/store/order/integration/ExchangeGateway.java`
- `order-service/src/main/resources/db/migration/V1__create_orders_schema.sql`
- `order-service/src/test/java/store/order/OrderResourceIntegrationTest.java`

