# Order Service

Microservico Spring Boot responsavel por criacao e consulta de pedidos.

## Stack

- Java 21 / Spring Boot
- PostgreSQL com Flyway
- OpenFeign para Product Service e Exchange Service
- Prometheus via Spring Actuator

## Como executar

```bash
./mvnw spring-boot:run
```

Por padrao a aplicacao espera PostgreSQL e os servicos dependentes. Para subir tudo junto, use o compose da raiz deste repositorio:

```bash
cd ..
docker compose up -d --build db product-service exchange-service order-service
```

## Endpoints

- `POST /orders`
- `GET /orders`
- `GET /orders/{id}`

## Testes

```bash
./mvnw test
```

Os testes usam H2 em memoria e mocks para as dependencias externas.

## Documentacao

```bash
cd ..
pip install -r requirements-docs.txt
mkdocs serve
```
