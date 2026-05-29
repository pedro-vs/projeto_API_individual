# Bottlenecks

## Bottleneck 1: leituras repetidas no catalogo

Durante a criacao de pedidos, o `order-service` consulta produtos para validar existencia e preco. Em uma carga maior, leituras repetidas em `GET /products` e `GET /products/{id}` pressionariam o banco.

### Mitigacao

O `product-service` usa Redis com Spring Cache.

Arquivos:

- `product-service/src/main/java/store/product/config/CacheConfig.java`;
- `product-service/src/main/java/store/product/service/ProductService.java`;
- `product-service/src/main/resources/application.properties`.

## Bottleneck 2: escalabilidade de entrada e pedidos

O gateway e o servico de pedidos tendem a receber mais carga durante fluxos de compra.

### Mitigacao

Os manifests Kubernetes incluem recursos e HPA nos servicos mais sensiveis:

- `gateway-service/k8s/hpa.yaml`;
- `product-service/k8s/hpa.yaml`;
- `order-service/k8s/hpa.yaml`.

## Observabilidade

As APIs Java expoem metricas via Spring Actuator e Micrometer Prometheus. Isso permite acompanhar consumo e comportamento durante os testes de carga do projeto em grupo.

Endpoints relevantes:

- `GET /actuator/prometheus` no `product-service`;
- `GET /actuator/prometheus` no `order-service`.
