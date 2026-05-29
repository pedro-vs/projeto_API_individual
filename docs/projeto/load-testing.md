# Load Testing

O teste de carga foi preparado para demonstrar o HPA escalando os servicos no Kubernetes.

## Ferramenta

O repositorio usa `k6`.

Arquivo principal:

```text
load-testing/k6/gateway-flow.js
```

O cenario executa:

- registro de usuario;
- login;
- criacao de produto;
- criacao de pedido;
- consulta de produtos.

## Como executar

Com o cluster EKS ou local ativo:

```bash
./scripts/load-testing/watch-hpa.sh
```

Em outro terminal:

```bash
./scripts/load-testing/run-k6.sh
```

Por padrao o script faz `port-forward` do `gateway-service` para:

```text
http://127.0.0.1:18085
```

Se o gateway estiver exposto por Load Balancer:

```bash
# edite BASE_URL no .env ou exporte temporariamente
export BASE_URL=http://<load-balancer>
./scripts/load-testing/run-k6.sh
```

## Evidencia esperada para o video

Grave a tela mostrando:

- `kubectl get hpa,pods -w`;
- execucao do `k6`;
- aumento de replicas em `product-service`, `order-service` ou `gateway-service`;
- estabilizacao ou reducao das replicas apos o teste.

## Arquivos adicionados

- `load-testing/k6/gateway-flow.js`
- `scripts/load-testing/run-k6.sh`
- `scripts/load-testing/watch-hpa.sh`
