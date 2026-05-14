from pydantic import BaseModel, ConfigDict, Field


class ExchangeOut(BaseModel):
    sell: float
    buy: float
    date: str
    id_account: str = Field(alias="id-account")

    model_config = ConfigDict(populate_by_name=True)


class HealthOut(BaseModel):
    service: str
    status: str


class ProblemOut(BaseModel):
    detail: str
    status: int
    title: str
    type: str = "about:blank"
