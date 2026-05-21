# Execucao local

## Subir a stack

```bash
docker compose up -d --build
```

Servicos:

- `product-service`: porta `8080`.
- `order-service`: porta `8081`.
- `exchange-service`: porta `8000`, mock local.
- `db`: PostgreSQL disponivel na rede interna do Compose.
- `redis`: Redis disponivel na rede interna do Compose.

## Fluxo manual

Criar produto:

```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Coffee","price":10.50,"unit":"kg"}'
```

Criar pedido:

```bash
curl -X POST http://localhost:8081/orders \
  -H "Content-Type: application/json" \
  -H "id-account: demo-user" \
  -d '{"items":[{"idProduct":"ID_DO_PRODUTO","quantity":2}]}'
```

## Validacao automatizada

```bash
./scripts/check-local.sh
```

## Observabilidade opcional

```bash
docker compose --profile observability up -d --build
```

- Prometheus: `http://localhost:9090`
- Grafana: `http://localhost:3000`
