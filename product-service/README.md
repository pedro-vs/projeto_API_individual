# Product Service

Microservico Spring Boot responsavel por cadastro, consulta e remocao de produtos.

## Stack

- Java 21 / Spring Boot
- PostgreSQL com Flyway
- Redis para cache
- Prometheus via Spring Actuator

## Como executar

```bash
./mvnw spring-boot:run
```

Por padrao a aplicacao espera PostgreSQL. Para subir tudo junto, use o compose da raiz deste repositorio:

```bash
cd ..
docker compose up -d --build db redis product-service
```

## Endpoints

- `POST /products`
- `GET /products`
- `GET /products/{id}`
- `DELETE /products/{id}`

Swagger UI: http://localhost:8080/swagger-ui.html

## Testes

```bash
./mvnw test
```

Os testes usam H2 em memoria por configuracao propria em `src/test/resources`.

## Documentacao

```bash
cd ..
pip install -r requirements-docs.txt
mkdocs serve
```
