# Order Architecture

```mermaid
flowchart LR
    Client[Gateway] --> Resource[Order Resource]
    Resource --> Service[Order Service]
    Service --> Product[Product Service]
    Service --> Exchange[Exchange Service]
    Service --> Repository[Order Repository]
    Repository --> Database[(PostgreSQL)]
```

## Persistencia

O Flyway cria o schema `orders` com as tabelas:

- `orders.orders`;
- `orders.order_items`.

## Chamadas entre servicos

O `order-service` usa OpenFeign para chamar:

- `product-service`, durante a criacao do pedido;
- `exchange-service`, quando o cliente solicita conversao de moeda.

## Propagacao de identidade

O header `id-account` representa a conta autenticada e e usado para criar, listar e consultar pedidos apenas do usuario correto.
