from dataclasses import dataclass
import os


@dataclass(frozen=True)
class Settings:
    awesome_api_base_url: str = os.getenv(
        "AWESOME_API_BASE_URL",
        "https://economia.awesomeapi.com.br/json/last",
    )
    awesome_api_key: str = os.getenv("AWESOME_API_KEY", "")
    awesome_api_timeout_seconds: float = float(
        os.getenv("AWESOME_API_TIMEOUT_SECONDS", "5")
    )


settings = Settings()
