# Atividades Realizadas

## Microservicos individuais

Minha entrega individual contempla dois microservicos:

- `product-service`;
- `order-service`.

Essa divisao aconteceu porque o grupo possui dois integrantes: Raphael ficou com `exchange-service`, enquanto eu fiquei com Product API e Order API.

## Product API

Entregas principais:

- `POST /products`;
- `GET /products`;
- `GET /products/{id}`;
- `DELETE /products/{id}`;
- validacao de payload;
- tratamento de produto inexistente;
- persistencia com PostgreSQL, JPA e Flyway;
- cache de leitura com Redis;
- metricas com Spring Actuator e Prometheus;
- testes automatizados com H2.

Arquivos principais:

- `product-service/src/main/java/store/product/resource/ProductResource.java`;
- `product-service/src/main/java/store/product/service/ProductService.java`;
- `product-service/src/main/resources/db/migration/V1__create_products_schema.sql`;
- `product-service/src/test/java/store/product/ProductResourceIntegrationTest.java`;
- `product-service/Dockerfile`;
- `product-service/Jenkinsfile`;
- `product-service/k8s/`.

## Order API

Entregas principais:

- `POST /orders`;
- `GET /orders`;
- `GET /orders/{id}`;
- uso do header `id-account`;
- validacao de produtos via `product-service`;
- calculo de totais;
- conversao opcional de moeda via `exchange-service`;
- persistencia com PostgreSQL, JPA e Flyway;
- metricas com Spring Actuator e Prometheus;
- testes automatizados com H2 e mocks das dependencias.

Arquivos principais:

- `order-service/src/main/java/store/order/resource/OrderResource.java`;
- `order-service/src/main/java/store/order/service/OrderService.java`;
- `order-service/src/main/java/store/order/integration/ProductGateway.java`;
- `order-service/src/main/java/store/order/integration/ExchangeGateway.java`;
- `order-service/src/main/resources/db/migration/V1__create_orders_schema.sql`;
- `order-service/src/test/java/store/order/OrderResourceIntegrationTest.java`;
- `order-service/Dockerfile`;
- `order-service/Jenkinsfile`;
- `order-service/k8s/`.

## Atividades do projeto em grupo

O projeto em grupo consolidou:

- `account-service`;
- `auth-service`;
- `gateway-service`;
- `product-service`;
- `order-service`;
- `exchange-service`;
- deploy com Docker, Kubernetes e EKS;
- pipelines Jenkins;
- load testing com k6;
- custos e PaaS;
- apresentacao final.

Essas partes ficam documentadas no repositorio do grupo:

- <https://github.com/insper-aulas/micro_api_26.1>
- <https://insper-aulas.github.io/micro_api_26.1/>
