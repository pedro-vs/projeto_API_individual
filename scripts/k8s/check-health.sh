#!/usr/bin/env bash

set -euo pipefail

NAMESPACE="${NAMESPACE:-store-platform}"

for deployment in postgres redis exchange-service product-service order-service; do
  kubectl -n "${NAMESPACE}" rollout status "deployment/${deployment}" --timeout=180s
done

kubectl -n "${NAMESPACE}" get pods
kubectl -n "${NAMESPACE}" get services

echo "Kubernetes deployments are healthy."

