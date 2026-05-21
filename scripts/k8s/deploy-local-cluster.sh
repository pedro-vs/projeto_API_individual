#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
NAMESPACE="store-platform"
PRODUCT_IMAGE="store/product-service:local"
ORDER_IMAGE="store/order-service:local"
EXCHANGE_IMAGE="store/mock-exchange-service:local"

cd "${ROOT_DIR}"

docker build -t "${PRODUCT_IMAGE}" product-service
docker build -t "${ORDER_IMAGE}" order-service
docker build -t "${EXCHANGE_IMAGE}" support/mock-exchange

if command -v kind >/dev/null 2>&1 && kind get clusters | grep -qx "store"; then
  kind load docker-image "${PRODUCT_IMAGE}" --name store
  kind load docker-image "${ORDER_IMAGE}" --name store
  kind load docker-image "${EXCHANGE_IMAGE}" --name store
fi

kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/postgres
kubectl apply -f k8s/redis
kubectl -n "${NAMESPACE}" rollout status deployment/postgres --timeout=180s
kubectl -n "${NAMESPACE}" rollout status deployment/redis --timeout=180s

for manifest in k8s/exchange-service/*.yaml; do
  sed "s|IMAGE_PLACEHOLDER|${EXCHANGE_IMAGE}|g" "${manifest}" | kubectl apply -f -
done

for service_dir in product-service order-service; do
  case "${service_dir}" in
    product-service) image="${PRODUCT_IMAGE}" ;;
    order-service) image="${ORDER_IMAGE}" ;;
  esac

  for manifest in configmap.yaml secrets.yaml deployment.yaml service.yaml hpa.yaml; do
    if [ -f "${service_dir}/k8s/${manifest}" ]; then
      sed "s|IMAGE_PLACEHOLDER|${image}|g" "${service_dir}/k8s/${manifest}" | kubectl apply -f -
    fi
  done
done

kubectl -n "${NAMESPACE}" rollout status deployment/exchange-service --timeout=180s
kubectl -n "${NAMESPACE}" rollout status deployment/product-service --timeout=180s
kubectl -n "${NAMESPACE}" rollout status deployment/order-service --timeout=180s
kubectl -n "${NAMESPACE}" get pods

