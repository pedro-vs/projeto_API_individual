# Bottlenecks

## 1. Cache na Product API

A Product API usa Redis com Spring Cache para reduzir leituras repetidas no banco.

- Cache de listagem de produtos.
- Cache de consulta por ID.
- Invalidacao ao criar ou remover produtos.

## 2. HPA em Kubernetes

Product API e Order API possuem `HorizontalPodAutoscaler`.

- Replica minima: 1.
- Replica maxima: 4.
- Escala por uso medio de CPU.

## 3. Observabilidade

Os servicos Java expoem metricas via Spring Actuator e Prometheus:

- `GET /actuator/prometheus`
- tags de aplicacao por microservico
- suporte a Prometheus/Grafana

