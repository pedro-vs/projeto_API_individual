# Projeto API Individual

Etapas implementadas: `Product API`, `Order API` e `Exchange API`.

## Estrutura atual

- `product-service`: microservico Spring Boot responsavel pelos produtos.
- `order-service`: microservico Spring Boot responsavel pelos pedidos.
- `exchange-service`: microservico FastAPI responsavel pelas cotacoes.

## Como rodar

```bash
cd product-service
./mvnw spring-boot:run
```

O `product-service` sobe por padrao em `http://localhost:8080`.

```bash
cd order-service
./mvnw spring-boot:run
```

O `order-service` sobe por padrao em `http://localhost:8081`.

```bash
cd exchange-service
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

O `exchange-service` sobe por padrao em `http://localhost:8000`.

## Endpoints disponiveis

### Product API

- `POST /products`
- `GET /products`
- `GET /products/{id}`
- `DELETE /products/{id}`

### Order API

- `POST /orders`
- `GET /orders`
- `GET /orders/{id}`

### Exchange API

- `GET /exchanges/{from}/{to}`

## Exemplo de criacao

```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tomato",
    "price": 10.12,
    "unit": "kg"
  }'
```

## Exemplo de pedido

O `order-service` usa o header `id-account` para identificar o usuario autenticado, seguindo o padrao descrito na materia.

```bash
curl -X POST http://localhost:8081/orders \
  -H "Content-Type: application/json" \
  -H "id-account: 70" \
  -d '{
    "items": [
      {
        "idProduct": "9db5f5fe-4a89-4e80-b03f-3d6e4f6e6cf1",
        "quantity": 2
      }
    ]
  }'
```

## Dependencias entre servicos

- Para criar pedidos, o `order-service` consulta o `product-service`.
- Para converter o total com `GET /orders/{id}?currency=BRL`, o `order-service` espera uma `exchange-service` em `http://localhost:8000`.
- A `exchange-service` consulta a `AwesomeAPI` para obter `bid` e `ask` em tempo real.
