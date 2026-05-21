#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
NAMESPACE="${NAMESPACE:-store-platform}"
LOCAL_KIND_BIN="${ROOT_DIR}/scripts/k8s/bin/kind"

ACCOUNT_IMAGE="${ACCOUNT_IMAGE:-local/account-service:minikube}"
AUTH_IMAGE="${AUTH_IMAGE:-local/auth-service:minikube}"
GATEWAY_IMAGE="${GATEWAY_IMAGE:-local/gateway-service:minikube}"
PRODUCT_IMAGE="${PRODUCT_IMAGE:-local/product-service:minikube}"
ORDER_IMAGE="${ORDER_IMAGE:-local/order-service:minikube}"
EXCHANGE_IMAGE="${EXCHANGE_IMAGE:-local/exchange-service:minikube}"

kind_cmd() {
  if command -v kind >/dev/null 2>&1; then
    command kind "$@"
    return
  fi

  if [[ -x "${LOCAL_KIND_BIN}" ]]; then
    "${LOCAL_KIND_BIN}" "$@"
    return
  fi

  return 127
}

build_images() {
  docker build -t "${ACCOUNT_IMAGE}" "${ROOT_DIR}/account-service"
  docker build -t "${AUTH_IMAGE}" "${ROOT_DIR}/auth-service"
  docker build -t "${GATEWAY_IMAGE}" "${ROOT_DIR}/gateway-service"
  docker build -t "${PRODUCT_IMAGE}" "${ROOT_DIR}/product-service"
  docker build -t "${ORDER_IMAGE}" "${ROOT_DIR}/order-service"
  docker build -t "${EXCHANGE_IMAGE}" "${ROOT_DIR}/exchange-service"
}

load_images_if_possible() {
  if kind_cmd version >/dev/null 2>&1; then
    local current_context
    local cluster_name
    current_context="$(kubectl config current-context 2>/dev/null || true)"
    if [[ "${current_context}" == kind-* ]]; then
      cluster_name="${current_context#kind-}"
      kind_cmd load docker-image --name "${cluster_name}" "${ACCOUNT_IMAGE}"
      kind_cmd load docker-image --name "${cluster_name}" "${AUTH_IMAGE}"
      kind_cmd load docker-image --name "${cluster_name}" "${GATEWAY_IMAGE}"
      kind_cmd load docker-image --name "${cluster_name}" "${PRODUCT_IMAGE}"
      kind_cmd load docker-image --name "${cluster_name}" "${ORDER_IMAGE}"
      kind_cmd load docker-image --name "${cluster_name}" "${EXCHANGE_IMAGE}"
    fi
  fi

  if command -v minikube >/dev/null 2>&1; then
    minikube image load "${ACCOUNT_IMAGE}" || true
    minikube image load "${AUTH_IMAGE}" || true
    minikube image load "${GATEWAY_IMAGE}" || true
    minikube image load "${PRODUCT_IMAGE}" || true
    minikube image load "${ORDER_IMAGE}" || true
    minikube image load "${EXCHANGE_IMAGE}" || true
  fi
}

apply_service_manifests() {
  local image="$1"
  local manifest_dir="$2"
  local manifest

  for manifest in configmap.yaml secrets.yaml deployment.yaml service.yaml hpa.yaml; do
    if [[ -f "${manifest_dir}/${manifest}" ]]; then
      sed "s|IMAGE_PLACEHOLDER|${image}|g" "${manifest_dir}/${manifest}" | kubectl apply -f -
    fi
  done
}

main() {
  build_images
  load_images_if_possible

  kubectl apply -f "${ROOT_DIR}/k8s/namespace.yaml"

  kubectl apply -f "${ROOT_DIR}/k8s/postgres"
  kubectl apply -f "${ROOT_DIR}/k8s/redis"

  kubectl -n "${NAMESPACE}" rollout status deployment/postgres --timeout=180s
  kubectl -n "${NAMESPACE}" rollout status deployment/redis --timeout=180s

  apply_service_manifests "${ACCOUNT_IMAGE}" "${ROOT_DIR}/account-service/k8s"
  apply_service_manifests "${AUTH_IMAGE}" "${ROOT_DIR}/auth-service/k8s"
  apply_service_manifests "${GATEWAY_IMAGE}" "${ROOT_DIR}/gateway-service/k8s"
  apply_service_manifests "${PRODUCT_IMAGE}" "${ROOT_DIR}/product-service/k8s"
  apply_service_manifests "${ORDER_IMAGE}" "${ROOT_DIR}/order-service/k8s"
  apply_service_manifests "${EXCHANGE_IMAGE}" "${ROOT_DIR}/exchange-service/k8s"

  kubectl -n "${NAMESPACE}" rollout status deployment/account-service --timeout=180s
  kubectl -n "${NAMESPACE}" rollout status deployment/auth-service --timeout=180s
  kubectl -n "${NAMESPACE}" rollout status deployment/gateway-service --timeout=180s
  kubectl -n "${NAMESPACE}" rollout status deployment/product-service --timeout=180s
  kubectl -n "${NAMESPACE}" rollout status deployment/order-service --timeout=180s
  kubectl -n "${NAMESPACE}" rollout status deployment/exchange-service --timeout=180s

  kubectl -n "${NAMESPACE}" get pods,svc
}

main "$@"
