# Product Development

## Testes

```bash
cd product-service
./mvnw test
```

Os testes usam H2 em memoria por configuracao propria em `src/test/resources/application.properties`.

## Docker

```bash
cd product-service
docker build -t local/product-service .
```

## Kubernetes

```bash
kubectl apply -f product-service/k8s/
```

## Variaveis de ambiente

| Variavel | Descricao |
| --- | --- |
| `DATABASE_HOST` | Host do PostgreSQL |
| `DATABASE_PORT` | Porta do PostgreSQL |
| `DATABASE_DB` | Nome do banco |
| `DATABASE_USERNAME` | Usuario do banco |
| `DATABASE_PASSWORD` | Senha do banco |
| `REDIS_HOST` | Host do Redis |
| `REDIS_PORT` | Porta do Redis |
