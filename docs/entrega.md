# Entrega

## Identificacao

- Aluno: `Pedro Henrique Vargas Sepulveda`
- Grupo: `Pedro Henrique Vargas Sepulveda` e `Raphael Cimerman Lafer`
- Microservicos individuais: `product-service` e `order-service`
- Repositorio individual: [`pedro-vs/projeto_API_individual`](https://github.com/pedro-vs/projeto_API_individual)
- Documentacao individual: <https://pedro-vs.github.io/projeto_API_individual/>
- Repositorio do projeto em grupo: [`insper-aulas/micro_api_26.1`](https://github.com/insper-aulas/micro_api_26.1)
- Documentacao do projeto em grupo: <https://insper-aulas.github.io/micro_api_26.1/>

## Itens cobertos nesta documentacao

- Nome do aluno e identificacao da entrega.
- Documentacao das atividades individuais realizadas.
- Codigo fonte das APIs individuais `product-service` e `order-service`.
- Testes, Dockerfile, Jenkinsfile e manifests Kubernetes das APIs.
- Links para o projeto em grupo e para os repositorios utilizados.
- Destaques dos bottlenecks implementados.
- Referencia para apresentacao, video e evidencias compartilhadas do projeto.

## Itens pedidos

| Item | Status | Onde esta |
| --- | --- | --- |
| Nome do aluno e grupo | Feito | Esta pagina e [Inicio](index.md) |
| Documentacao das atividades realizadas | Feito | [Atividades](atividades.md) |
| Codigo fonte das atividades realizadas | Feito | `product-service/` e `order-service/` |
| Documentacao do projeto | Feito | [Projeto em Grupo](projeto.md) e site do grupo |
| Codigo fonte do projeto | Feito | [Repositorios](repositorios.md) |
| Links para repositorios utilizados | Feito | [Repositorios](repositorios.md) |
| Bottlenecks implementados | Feito | [Bottlenecks](bottlenecks.md) |
| Apresentacao do projeto | Pendente de link externo | [Apresentacao](apresentacao.md) |
| Video de apresentacao | Pendente de link externo | [Apresentacao](apresentacao.md) |
| Uso de IA documentado | Feito | [Uso de IA](uso-ia.md) |

## Links importantes

- Repositorio individual: <https://github.com/pedro-vs/projeto_API_individual>
- Documentacao individual publicada: <https://pedro-vs.github.io/projeto_API_individual/>
- Repositorio do grupo: <https://github.com/insper-aulas/micro_api_26.1>
- Documentacao do grupo: <https://insper-aulas.github.io/micro_api_26.1/>
- Handout da disciplina: <https://insper.github.io/platform/versions/2026.1/>
- Sequencia do projeto:
  - <https://insper.github.io/platform/2026.1/exercises/project/>
  - <https://insper.github.io/platform/2026.1/exercises/project/aws/>
  - <https://insper.github.io/platform/2026.1/exercises/project/eks/>
  - <https://insper.github.io/platform/2026.1/exercises/project/ci-cd/>
  - <https://insper.github.io/platform/2026.1/exercises/project/load-testing/>
  - <https://insper.github.io/platform/2026.1/exercises/project/costs/>
  - <https://insper.github.io/platform/2026.1/exercises/project/presentation/>

## Apresentacao e video

- Slides: pendente de publicacao externa.
- Video: pendente de publicacao externa.
- Roteiro: documentado em [Apresentacao](apresentacao.md).

## Bottlenecks destacados

- `Caching`: o `product-service` usa Redis com Spring Cache para reduzir leituras repetidas no banco.
- `HPA e observabilidade`: `product-service` e `order-service` possuem manifests com recursos/HPA e metricas via Actuator/Prometheus.
