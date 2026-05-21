# MiniKube

## Objetivo

Executar os microservicos no mesmo cluster Kubernetes local, com manifests completos e verificacao de saude.

## Entregue

Cada microservico possui manifests separados dentro da pasta `k8s/`, facilitando manutencao e revisao:

- `Secret`
- `ConfigMap`
- `Deployment`
- `Service`
- `HorizontalPodAutoscaler` nos servicos com recursos configurados

Arquivos:

- `account-service/k8s/`
- `auth-service/k8s/`
- `gateway-service/k8s/`
- `product-service/k8s/`
- `order-service/k8s/`
- `exchange-service/k8s/`
- `k8s/namespace.yaml`
- `k8s/postgres/`
- `k8s/redis/`

## Scripts de apoio

- `scripts/k8s/deploy-local-cluster.sh`
- `scripts/k8s/check-health.sh`
- `scripts/k8s/kind-config.yaml`
- `scripts/k8s/install-kind.sh`

## Exemplo com `kind`

```bash
./scripts/k8s/install-kind.sh
./scripts/k8s/bin/kind create cluster --name store --config scripts/k8s/kind-config.yaml
./scripts/k8s/deploy-local-cluster.sh
./scripts/k8s/check-health.sh
```

## Validacao executada

Na revisao final em `2026-05-20`, os manifests foram checados com `kubectl apply --dry-run=client --validate=false`, incluindo PostgreSQL, Redis e os seis `Deployment` de servicos. Para validar com cluster real, execute o fluxo acima e finalize com `./scripts/k8s/check-health.sh`.

## O que o deploy faz

- builda as seis imagens locais
- carrega as imagens no cluster `kind` quando aplicavel
- aplica namespace, PostgreSQL, Redis e manifests dos servicos
- espera o rollout do PostgreSQL, Redis e dos seis `Deployment`
- lista `pods` e `services` ao final

## Aderencia ao enunciado

- Os servicos pedidos literalmente em `MiniKube` estao presentes: `account-service`, `auth-service`, `gateway-service`, `product-service` e `order-service`.
- O repositorio tambem publica `exchange-service` no mesmo cluster para manter a stack completa utilizada nas etapas anteriores.
