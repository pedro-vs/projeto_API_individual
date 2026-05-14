class ExchangeServiceError(Exception):
    """Base exception for the exchange service."""


class UnauthorizedRequestError(ExchangeServiceError):
    """Raised when the caller identity is missing."""


class UnsupportedCurrencyError(ExchangeServiceError):
    """Raised when a currency pair is invalid or unsupported."""


class UpstreamExchangeProviderError(ExchangeServiceError):
    """Raised when the external exchange provider fails."""
