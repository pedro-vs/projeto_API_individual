from __future__ import annotations

from typing import Optional

from fastapi import Depends, FastAPI, Header
from fastapi.responses import JSONResponse
from prometheus_fastapi_instrumentator import Instrumentator

from app.exceptions import (
    UnauthorizedRequestError,
    UnsupportedCurrencyError,
    UpstreamExchangeProviderError,
)
from app.models import ExchangeOut, HealthOut, ProblemOut
from app.services.exchange_service import ExchangeService

app = FastAPI(title="exchange-service", version="0.1.0")
Instrumentator(excluded_handlers=["/metrics"]).instrument(app).expose(
    app,
    include_in_schema=False,
)


def get_exchange_service() -> ExchangeService:
    return ExchangeService()


@app.exception_handler(UnauthorizedRequestError)
async def unauthorized_request_handler(_, exception: UnauthorizedRequestError) -> JSONResponse:
    return build_problem_response(
        title="Unauthorized request",
        detail=str(exception),
        status_code=401,
    )


@app.exception_handler(UnsupportedCurrencyError)
async def unsupported_currency_handler(_, exception: UnsupportedCurrencyError) -> JSONResponse:
    return build_problem_response(
        title="Unsupported currency",
        detail=str(exception),
        status_code=422,
    )


@app.exception_handler(UpstreamExchangeProviderError)
async def upstream_provider_handler(_, exception: UpstreamExchangeProviderError) -> JSONResponse:
    return build_problem_response(
        title="Exchange provider unavailable",
        detail=str(exception),
        status_code=502,
    )


@app.get("/", response_model=HealthOut)
def healthcheck() -> HealthOut:
    return HealthOut(service="exchange-service", status="ok")


@app.get("/exchanges/{from_currency}/{to_currency}", response_model=ExchangeOut)
@app.get(
    "/exchange/{from_currency}/{to_currency}",
    response_model=ExchangeOut,
    include_in_schema=False,
)
def get_exchange(
    from_currency: str,
    to_currency: str,
    id_account: Optional[str] = Header(default=None, alias="id-account"),
    service: ExchangeService = Depends(get_exchange_service),
) -> ExchangeOut:
    return service.get_exchange(from_currency, to_currency, id_account)


def build_problem_response(
    title: str, detail: str, status_code: int
) -> JSONResponse:
    payload = ProblemOut(title=title, detail=detail, status=status_code)
    return JSONResponse(status_code=status_code, content=payload.model_dump())
