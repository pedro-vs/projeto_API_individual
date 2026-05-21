#!/usr/bin/env bash

set -euo pipefail

PRODUCT_URL="${PRODUCT_URL:-http://localhost:8080}"
ORDER_URL="${ORDER_URL:-http://localhost:8081}"
EXCHANGE_URL="${EXCHANGE_URL:-http://localhost:8000}"
ACCOUNT_ID="${ACCOUNT_ID:-demo-user}"

wait_for_url() {
  local url="$1"
  local name="$2"

  for _ in $(seq 1 60); do
    if curl -fsS "${url}" >/dev/null 2>&1; then
      return 0
    fi
    sleep 2
  done

  echo "${name} did not become ready at ${url}" >&2
  return 1
}

echo "Checking health endpoints..."
wait_for_url "${PRODUCT_URL}/" "Product API"
wait_for_url "${ORDER_URL}/" "Order API"
wait_for_url "${EXCHANGE_URL}/" "Mock Exchange"

echo "Creating product..."
PRODUCT_RESPONSE="$(curl -fsS -X POST "${PRODUCT_URL}/products" \
  -H "Content-Type: application/json" \
  -d '{"name":"Coffee","price":10.50,"unit":"kg"}')"

PRODUCT_ID="$(python3 -c 'import json,sys; print(json.load(sys.stdin)["id"])' <<<"${PRODUCT_RESPONSE}")"

echo "Creating order for product ${PRODUCT_ID}..."
ORDER_RESPONSE="$(curl -fsS -X POST "${ORDER_URL}/orders" \
  -H "Content-Type: application/json" \
  -H "id-account: ${ACCOUNT_ID}" \
  -d "{\"items\":[{\"idProduct\":\"${PRODUCT_ID}\",\"quantity\":2}]}")"

ORDER_ID="$(python3 -c 'import json,sys; print(json.load(sys.stdin)["id"])' <<<"${ORDER_RESPONSE}")"

echo "Checking order list and converted detail..."
curl -fsS -H "id-account: ${ACCOUNT_ID}" "${ORDER_URL}/orders" >/dev/null
curl -fsS -H "id-account: ${ACCOUNT_ID}" "${ORDER_URL}/orders/${ORDER_ID}?currency=BRL" >/dev/null

echo "Local flow completed successfully."
