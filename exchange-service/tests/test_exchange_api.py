from __future__ import annotations

from unittest.mock import Mock, patch

from fastapi.testclient import TestClient

from app.main import app

client = TestClient(app)


def make_response(status_code: int, json_body: dict) -> Mock:
    response = Mock()
    response.status_code = status_code
    response.json.return_value = json_body
    return response


def test_get_exchange_returns_current_quote() -> None:
    mocked_response = make_response(
        200,
        {
            "USDBRL": {
                "bid": "5.0008",
                "ask": "5.0038",
                "create_date": "2026-05-14 12:35:24",
            }
        },
    )

    with patch("app.clients.awesome_api_client.requests.get", return_value=mocked_response) as requests_get:
        response = client.get(
            "/exchanges/usd/brl",
            headers={"id-account": "0195ae95-5be7-7dd3-b35d-7a7d87c404fb"},
        )

    assert response.status_code == 200
    assert response.json() == {
        "sell": 5.0038,
        "buy": 5.0008,
        "date": "2026-05-14 12:35:24",
        "id-account": "0195ae95-5be7-7dd3-b35d-7a7d87c404fb",
    }
    requests_get.assert_called_once_with(
        "https://economia.awesomeapi.com.br/json/last/USD-BRL",
        timeout=5.0,
    )


def test_get_exchange_requires_authenticated_account() -> None:
    response = client.get("/exchanges/USD/BRL")

    assert response.status_code == 401
    assert response.json()["title"] == "Unauthorized request"


def test_get_exchange_returns_unprocessable_entity_for_invalid_currency() -> None:
    response = client.get(
        "/exchanges/USDX/BRL",
        headers={"id-account": "70"},
    )

    assert response.status_code == 422
    assert response.json()["title"] == "Unsupported currency"


def test_get_exchange_returns_unprocessable_entity_when_provider_rejects_currency() -> None:
    mocked_response = make_response(
        404,
        {
            "status": 404,
            "code": "CoinNotExists",
            "message": "moeda nao encontrada USD-ZZZ",
        },
    )

    with patch("app.clients.awesome_api_client.requests.get", return_value=mocked_response):
        response = client.get(
            "/exchanges/USD/ZZZ",
            headers={"id-account": "70"},
        )

    assert response.status_code == 422
    assert response.json()["title"] == "Unsupported currency"


def test_get_exchange_returns_bad_gateway_when_provider_fails() -> None:
    mocked_response = make_response(500, {"message": "unexpected"})

    with patch("app.clients.awesome_api_client.requests.get", return_value=mocked_response):
        response = client.get(
            "/exchanges/USD/BRL",
            headers={"id-account": "70"},
        )

    assert response.status_code == 502
    assert response.json()["title"] == "Exchange provider unavailable"


def test_metrics_endpoint_is_exposed() -> None:
    response = client.get("/metrics")

    assert response.status_code == 200
    assert "http_requests_total" in response.text
