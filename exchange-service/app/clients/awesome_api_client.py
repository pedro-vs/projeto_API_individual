from __future__ import annotations

from typing import Any

import requests

from app.config import settings
from app.exceptions import (
    UnsupportedCurrencyError,
    UpstreamExchangeProviderError,
)


class AwesomeApiClient:
    def __init__(
        self,
        base_url: str = settings.awesome_api_base_url,
        timeout_seconds: float = settings.awesome_api_timeout_seconds,
    ) -> None:
        self.base_url = base_url.rstrip("/")
        self.timeout_seconds = timeout_seconds

    def get_quote(self, from_currency: str, to_currency: str) -> dict[str, Any]:
        pair = f"{from_currency}-{to_currency}"
        url = f"{self.base_url}/{pair}"

        try:
            response = requests.get(url, timeout=self.timeout_seconds)
        except requests.RequestException as exception:
            raise UpstreamExchangeProviderError(
                "could not reach the external exchange provider"
            ) from exception

        if response.status_code == 404:
            raise UnsupportedCurrencyError(f"currency pair {pair} is not supported")

        if response.status_code != 200:
            raise UpstreamExchangeProviderError(
                "external exchange provider returned an unexpected response"
            )

        body = response.json()
        quote_key = f"{from_currency}{to_currency}"
        quote = body.get(quote_key)

        if quote is None:
            raise UpstreamExchangeProviderError(
                "external exchange provider returned an invalid payload"
            )

        return quote
