# Projeto Individual - Product API e Order API

Entrega individual da disciplina Platforms, Microservices, DevOps and APIs.

- Aluno: Pedro Henrique Vargas Sepulveda
- APIs individuais: Product API e Order API
- Repositorio: https://github.com/pedro-vs/projeto_API_individual

## Estrutura

```text
product-service/      Product API em Spring Boot
order-service/        Order API em Spring Boot
support/mock-exchange Mock simples para validar conversao no Order localmente
docs/                 Documentacao MkDocs da entrega individual
k8s/                  Infra compartilhada para execucao em Kubernetes local
scripts/              Scripts de validacao/deploy local
compose.yaml          Stack local com PostgreSQL, Redis, Product, Order e mock Exchange
```

## Testes

```bash
cd product-service
./mvnw -B test

cd ../order-service
./mvnw -B test
```

## Execucao local

```bash
docker compose up -d --build
./scripts/check-local.sh
```

Portas principais:

- Product API: http://localhost:8080
- Order API: http://localhost:8081
- Mock Exchange: http://localhost:8000
- PostgreSQL e Redis ficam disponiveis apenas na rede interna do Docker Compose.

Swagger UI:

- Product API: http://localhost:8080/swagger-ui.html
- Order API: http://localhost:8081/swagger-ui.html

## Documentacao

```bash
python3 -m venv .venv-docs
source .venv-docs/bin/activate
pip install -r requirements-docs.txt
mkdocs serve
```

Build estrito:

```bash
mkdocs build --strict
```

## Kubernetes local

Os manifests de `product-service/`, `order-service/`, `k8s/postgres`, `k8s/redis` e `k8s/exchange-service` permitem validar a entrega em cluster local.

```bash
./scripts/k8s/deploy-local-cluster.sh
```
