# Validacao

## Testes automatizados

Comandos utilizados:

```bash
cd account-service && ./mvnw -B test
cd auth-service && ./mvnw -B test
cd gateway-service && ./mvnw -B test
cd product-service && ./mvnw -B test
cd order-service && ./mvnw -B test
cd exchange-service && pytest -q
```

Resultado observado em `2026-05-20`:

- `account-service`: `6` testes passando
- `auth-service`: `5` testes passando
- `gateway-service`: `5` testes passando
- `product-service`: `8` testes passando
- `order-service`: `9` testes passando
- `exchange-service`: `6` testes passando

Os testes de `account-service`, `product-service` e `order-service` usam H2 apenas em profile de teste, com Flyway e `ddl-auto=validate`, para validar o mesmo modelo usado em PostgreSQL.

## Validacao funcional local

Fluxo recomendado para validar a stack com `docker compose up -d --build`:

- Healthcheck em `GET /` para `account-service`, `gateway-service`, `product-service`, `order-service` e `exchange-service`, e em `GET /auth/` para `auth-service`.
- Cadastro em `POST /auth/register`.
- Login em `POST /auth/login` com recebimento do cookie JWT.
- Consulta de identidade em `GET /auth/whoami`.
- Criacao de produto em `POST /products` via `gateway-service`.
- Criacao de pedido em `POST /orders` via `gateway-service`.
- Consulta de pedido em `GET /orders/{id}` via `gateway-service`.
- Conversao de moeda em `GET /orders/{id}?currency=BRL` via `gateway-service`.
- Consulta de cambio em `GET /exchanges/USD/BRL` via `gateway-service`.
- Coleta de metricas em:
  - `GET /actuator/prometheus` no `account-service`
  - `GET /actuator/prometheus` no `auth-service`
  - `GET /actuator/prometheus` no `gateway-service`
  - `GET /actuator/prometheus` no `product-service`
  - `GET /actuator/prometheus` no `order-service`
  - `GET /metrics` no `exchange-service`

## Validacao de observabilidade

Os seguintes endpoints foram checados localmente:

- Prometheus health: `http://localhost:9090/-/healthy`
- Grafana health: `http://localhost:3000/api/health`

## Validacao de cluster local

Arquivos de orquestracao versionados:

- `k8s/namespace.yaml`
- `k8s/postgres/`
- `k8s/redis/`
- `account-service/k8s/`
- `auth-service/k8s/`
- `gateway-service/k8s/`
- `product-service/k8s/`
- `order-service/k8s/`
- `exchange-service/k8s/`

Scripts:

- `scripts/k8s/deploy-local-cluster.sh`
- `scripts/k8s/check-health.sh`
- `scripts/k8s/kind-config.yaml`
- `scripts/k8s/install-kind.sh`

Fluxo recomendado para validar em cluster local com `kind-store`:

```bash
./scripts/k8s/install-kind.sh
./scripts/k8s/bin/kind create cluster --name store --config scripts/k8s/kind-config.yaml
./scripts/k8s/deploy-local-cluster.sh
./scripts/k8s/check-health.sh
```

Na revisao final em `2026-05-20`, os manifests foram validados com:

```bash
kubectl apply --dry-run=client --validate=false -f k8s/namespace.yaml -f k8s/postgres -f k8s/redis -f account-service/k8s -f auth-service/k8s -f gateway-service/k8s -f product-service/k8s -f order-service/k8s -f exchange-service/k8s
```

Resultado observado:

- todos os manifests foram aceitos pelo `kubectl` em `dry-run`
- `docker compose build` concluiu o build das seis imagens da stack
- `mkdocs build --strict` concluiu a geracao da documentacao

## Publicacao da documentacao

O repositorio agora possui `mkdocs.yml`, `requirements-docs.txt` e a pipeline `.github/workflows/docs.yml`. Ao publicar no ramo `main`, o `GitHub Pages` pode gerar este site automaticamente.
