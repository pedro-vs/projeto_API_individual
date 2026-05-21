# Projeto API Individual

Repositorio da entrega individual de **Platforms, Microservices, DevOps and APIs** para a trilha `Exercises > Individual`.

Etapas cobertas:

- `Product API`
- `Order API`
- `Exchange API`
- `Jenkins`
- `MiniKube`
- `Bottlenecks`

## Arquitetura atual

O projeto hoje implementa os servicos pedidos literalmente nas etapas de `Jenkins` e `MiniKube`, alem do `exchange-service` da etapa especifica de cambio:

- `account-service`: cadastro, busca por id e validacao de credenciais.
- `auth-service`: registro, login, logout, resolucao de JWT e `whoami`.
- `gateway-service`: ponto de entrada da aplicacao, com roteamento e injecao de `id-account`.
- `product-service`: CRUD de produtos com cache de leitura em Redis.
- `order-service`: criacao e consulta de pedidos, incluindo conversao opcional de moeda.
- `exchange-service`: API Python para cotacoes, consumindo AwesomeAPI.

Infra compartilhada:

- `compose.yaml`: stack local com os seis servicos, PostgreSQL, Redis, Prometheus e Grafana.
- `k8s/`: namespace compartilhado, PostgreSQL e Redis para o cluster local.
- `scripts/k8s/`: deploy e verificacao em cluster local.
- `observability/`: configuracoes de Prometheus e Grafana.
- `docs/`, `mkdocs.yml`, `.github/workflows/docs.yml`: documentacao publicada com MkDocs e GitHub Pages.

## Como rodar localmente

### Stack completa

```bash
docker compose up -d --build
```

Servicos publicados:

- `gateway-service`: <http://localhost:8085>
- `account-service`: <http://localhost:8083>
- `auth-service`: <http://localhost:8084>
- `product-service`: <http://localhost:8080>
- `order-service`: <http://localhost:8081>
- `exchange-service`: <http://localhost:8000>
- `prometheus`: <http://localhost:9090>
- `grafana`: <http://localhost:3000>
- `redis`: `localhost:6379`
- `postgres`: `localhost:5432`

Entrada recomendada para uso funcional:

- `gateway-service`: autentica via cookie JWT e propaga `id-account` para os servicos internos.

### Servicos isolados

```bash
cd account-service && ./mvnw spring-boot:run
cd auth-service && ./mvnw spring-boot:run
cd gateway-service && ./mvnw spring-boot:run
cd product-service && ./mvnw spring-boot:run
cd order-service && ./mvnw spring-boot:run
cd exchange-service && python3 -m venv .venv && source .venv/bin/activate && pip install -r requirements.txt && uvicorn app.main:app --reload --port 8000
```

## Endpoints principais

### `account-service`

- `POST /accounts`
- `GET /accounts/{id}`
- `POST /accounts/search`

### `auth-service`

- `POST /auth/register`
- `POST /auth/login`
- `GET /auth/logout`
- `POST /auth/solve`
- `GET /auth/whoami`

### `product-service`

- `POST /products`
- `GET /products`
- `GET /products/{id}`
- `DELETE /products/{id}`

### `order-service`

- `POST /orders`
- `GET /orders`
- `GET /orders/{id}`

### `exchange-service`

- `GET /exchanges/{from}/{to}`

## Exemplo pelo gateway

Registrar:

```bash
curl -X POST http://localhost:8085/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice",
    "email": "alice@example.com",
    "password": "123456"
  }'
```

Login com cookie:

```bash
curl -i -c cookies.txt \
  -X POST http://localhost:8085/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alice@example.com",
    "password": "123456"
  }'
```

Criacao de produto:

```bash
curl -b cookies.txt \
  -X POST http://localhost:8085/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tomato",
    "price": 10.12,
    "unit": "kg"
  }'
```

## Dependencias entre servicos

- `auth-service` consome `account-service`.
- `gateway-service` consome `auth-service` para resolver o JWT e encaminha chamadas para os demais servicos.
- `order-service` consome `product-service` para validar itens e calcular totais.
- `order-service` consome `exchange-service` para `GET /orders/{id}?currency=...`.
- `exchange-service` consome AwesomeAPI.
- `account-service`, `product-service` e `order-service` usam PostgreSQL com migrations Flyway.

## Bottlenecks implementados

- `Caching`: `product-service` usa Spring Cache com Redis para `GET /products` e `GET /products/{id}`.
- `Observability`: `account-service`, `auth-service`, `gateway-service`, `product-service` e `order-service` expoem `GET /actuator/prometheus`, e `exchange-service` expoe `GET /metrics`.

Arquivos principais:

- `compose.yaml`
- `observability/prometheus/prometheus.yml`
- `observability/grafana/provisioning/datasources/datasources.yml`
- `product-service/src/main/java/store/product/config/CacheConfig.java`
- `exchange-service/app/main.py`

Login padrao do Grafana:

- usuario: `admin`
- senha: `admin`

## Jenkins e Kubernetes

Cada microservico possui seu proprio `Jenkinsfile`:

- `account-service/Jenkinsfile`
- `auth-service/Jenkinsfile`
- `gateway-service/Jenkinsfile`
- `product-service/Jenkinsfile`
- `order-service/Jenkinsfile`
- `exchange-service/Jenkinsfile`

Os pipelines seguem os estagios pedidos no enunciado:

- `SCM`
- `Dependencies`
- `Build`
- `Push to Docker Hub`
- `Deploy to K8s`

Cada servico tambem possui:

- `Dockerfile`
- manifests em `k8s/` separados por responsabilidade: `configmap.yaml`, `secrets.yaml`, `deployment.yaml`, `service.yaml` e, nos servicos escalaveis, `hpa.yaml`

Credenciais esperadas no Jenkins:

- `dockerhub-credential`
- `kubeconfig`

Variavel esperada:

- `DOCKERHUB_NAMESPACE`

Namespace compartilhado:

- [namespace.yaml](/Users/robertosepulvedajr/projeto_API_individual/k8s/namespace.yaml:1)

## Cluster local

Fluxo com `kind`:

```bash
./scripts/k8s/install-kind.sh
./scripts/k8s/bin/kind create cluster --name store --config scripts/k8s/kind-config.yaml
./scripts/k8s/deploy-local-cluster.sh
./scripts/k8s/check-health.sh
```

Esses scripts:

- buildam as seis imagens locais
- carregam as imagens no cluster
- aplicam namespace, PostgreSQL, Redis e manifests dos servicos
- aguardam o rollout do PostgreSQL, Redis e dos seis `Deployment`
- validam healthchecks e um fluxo real pelo `gateway`

## Documentacao em MkDocs

Rodando localmente:

```bash
python3 -m venv .venv-docs
source .venv-docs/bin/activate
pip install -r requirements-docs.txt
mkdocs serve
```

Publicacao automatica:

- `.github/workflows/docs.yml`

## Validacao executada

Na revisao final em `2026-05-20` foram validados:

- testes automatizados dos seis servicos
- `docker compose config`
- `docker compose build`
- `mkdocs build --strict`
- `kubectl apply --dry-run=client --validate=false` dos manifests Kubernetes
