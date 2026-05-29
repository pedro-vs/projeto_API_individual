# Order Development

## Testes

```bash
cd order-service
./mvnw test
```

Os testes usam H2 em memoria e mocks para dependencias externas.

## Docker

```bash
cd order-service
docker build -t local/order-service .
```

## Kubernetes

```bash
kubectl apply -f order-service/k8s/
```

## Variaveis de ambiente

| Variavel | Descricao |
| --- | --- |
| `DATABASE_HOST` | Host do PostgreSQL |
| `DATABASE_PORT` | Porta do PostgreSQL |
| `DATABASE_DB` | Nome do banco |
| `DATABASE_USERNAME` | Usuario do banco |
| `DATABASE_PASSWORD` | Senha do banco |
| `PRODUCT_SERVICE_URL` | URL do Product Service |
| `EXCHANGE_SERVICE_URL` | URL do Exchange Service |
