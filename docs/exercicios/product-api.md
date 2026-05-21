# Product API

A Product API implementa o cadastro e consulta de produtos da plataforma.

## Endpoints

- `POST /products`: cria um produto.
- `GET /products`: lista produtos cadastrados.
- `GET /products/{id}`: consulta produto por ID.
- `DELETE /products/{id}`: remove produto por ID.
- `GET /`: healthcheck simples.
- `GET /actuator/prometheus`: metricas para Prometheus.

## Implementacao

- Spring Boot com Java 21.
- Camadas `resource`, `service`, `repository`, `model`, `dto` e `exception`.
- PostgreSQL em runtime.
- Flyway para criacao do schema `products`.
- H2 em testes automatizados.
- Redis com Spring Cache para reduzir leituras repetidas.

## Arquivos principais

- `product-service/src/main/java/store/product/resource/ProductResource.java`
- `product-service/src/main/java/store/product/service/ProductService.java`
- `product-service/src/main/resources/db/migration/V1__create_products_schema.sql`
- `product-service/src/test/java/store/product/ProductResourceIntegrationTest.java`

