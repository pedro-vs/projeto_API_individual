# Validacao

## Testes automatizados

```bash
cd product-service && ./mvnw -B test
cd order-service && ./mvnw -B test
```

## Documentacao

```bash
mkdocs build --strict
```

## Docker Compose

```bash
docker compose config
docker compose up -d --build
./scripts/check-local.sh
```

Observabilidade opcional:

```bash
docker compose --profile observability config
```

## Kubernetes

```bash
kubectl apply --dry-run=client --validate=false \
  -f k8s/namespace.yaml \
  -f k8s/postgres \
  -f k8s/redis \
  -f k8s/exchange-service \
  -f product-service/k8s \
  -f order-service/k8s
```
