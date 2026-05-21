from __future__ import annotations

from typing import Optional

from app.clients.awesome_api_client import AwesomeApiClient
from app.exceptions import UnauthorizedRequestError, UnsupportedCurrencyError
from app.models import ExchangeOut


class ExchangeService:
    def __init__(self, client: Optional[AwesomeApiClient] = None) -> None:
        self.client = client or AwesomeApiClient()

    def get_exchange(
        self, from_currency: str, to_currency: str, account_id: Optional[str]
    ) -> ExchangeOut:
        normalized_account_id = self._normalize_account_id(account_id)
        normalized_from = self._normalize_currency(from_currency)
        normalized_to = self._normalize_currency(to_currency)

        quote = self.client.get_quote(normalized_from, normalized_to)

        return ExchangeOut(
            sell=float(quote["ask"]),
            buy=float(quote["bid"]),
            date=quote["create_date"],
            id_account=normalized_account_id,
        )

    def _normalize_account_id(self, account_id: Optional[str]) -> str:
        if account_id is None or not account_id.strip():
            raise UnauthorizedRequestError("header id-account is required")

        return account_id.strip()

    def _normalize_currency(self, currency: str) -> str:
        normalized_currency = currency.strip().upper()

        if len(normalized_currency) != 3 or not normalized_currency.isalpha():
            raise UnsupportedCurrencyError(
                f"currency {currency} is not supported"
            )

        return normalized_currency
