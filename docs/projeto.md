# Projeto em Grupo

O projeto em grupo implementa uma plataforma de loja em microservicos. Minha contribuicao individual (`product-service` e `order-service`) foi integrada aos demais servicos:

- `account-service`;
- `auth-service`;
- `gateway-service`;
- `exchange-service`.

## Repositorio e documentacao

- Repositorio do grupo: <https://github.com/insper-aulas/micro_api_26.1>
- Documentacao do grupo: <https://insper-aulas.github.io/micro_api_26.1/>
- Documentacao individual de Raphael: <https://raphaellafer.github.io/projeto_individual_api/>

## Papel das minhas APIs no projeto

O `product-service` representa o catalogo de produtos. O `order-service` usa esse catalogo para validar itens e cria pedidos associados a uma conta autenticada. Quando o cliente solicita outra moeda, o `order-service` consulta o `exchange-service`, implementado por Raphael.

Fluxo resumido:

```text
gateway-service -> order-service -> product-service
                         |
                         v
                   exchange-service
```

## Evidencias compartilhadas

As evidencias de AWS, EKS, Jenkins, load testing, custos e video final ficam concentradas no site do grupo, para evitar duplicacao e manter a entrega coerente.
