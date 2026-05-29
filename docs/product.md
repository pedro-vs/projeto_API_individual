# Product API

O `product-service` e o microservico responsavel por criar, listar, consultar e remover produtos da loja.

## Responsabilidades

- armazenar o catalogo de produtos;
- validar payloads de entrada;
- disponibilizar produtos para o `order-service`;
- reduzir leituras repetidas com cache Redis;
- expor metricas e healthcheck.

## Stack

| Item | Tecnologia |
| --- | --- |
| Linguagem | Java 21 |
| Framework | Spring Boot |
| Persistencia | PostgreSQL, JPA e Flyway |
| Cache | Redis via Spring Cache |
| Observabilidade | Actuator e Prometheus |
| Testes | JUnit, MockMvc, H2 |

## Arquivos principais

- `product-service/src/main/java/store/product/resource/ProductResource.java`
- `product-service/src/main/java/store/product/service/ProductService.java`
- `product-service/src/main/java/store/product/config/CacheConfig.java`
- `product-service/src/main/resources/db/migration/V1__create_products_schema.sql`
- `product-service/src/test/java/store/product/ProductResourceIntegrationTest.java`
