# Projeto Individual

Esta documentacao descreve a entrega individual de **Product API** e **Order API**.

O repositorio contem os dois microservicos implementados em Spring Boot, testes automatizados, Dockerfiles, manifests Kubernetes, Jenkinsfiles, documentacao MkDocs e arquivos de suporte para execucao local.

## APIs implementadas

| API | Diretorio | Responsabilidade |
| --- | --- | --- |
| Product API | `product-service/` | Cadastro, consulta, listagem, remocao e cache de produtos |
| Order API | `order-service/` | Criacao e consulta de pedidos, integrando Product API e Exchange API |

## Destaques

- Persistencia em PostgreSQL com Flyway.
- Testes automatizados com H2 em profile de teste.
- Cache Redis na Product API.
- Metricas Prometheus via Spring Actuator.
- Manifests Kubernetes com requests, limits e HPA.

