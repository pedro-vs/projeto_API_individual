# MiniKube / Kubernetes

Os manifests Kubernetes foram separados por responsabilidade.

## Infra compartilhada

- `k8s/namespace.yaml`
- `k8s/postgres/`
- `k8s/redis/`
- `k8s/exchange-service/`

## Servicos individuais

- `product-service/k8s/`
- `order-service/k8s/`

## Deploy local

```bash
./scripts/k8s/deploy-local-cluster.sh
```

O script builda imagens locais, aplica PostgreSQL, Redis, mock Exchange, Product API e Order API, e aguarda o rollout dos deployments.

