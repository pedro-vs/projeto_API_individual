# Entrega

## Identificacao

- Aluno: Pedro Henrique Vargas Sepulveda
- Grupo: projeto em grupo com dois integrantes
- Entrega individual: Product API e Order API
- Repositorio individual: <https://github.com/pedro-vs/projeto_API_individual>

## Itens da rubrica

- Nome do aluno e grupo: documentado nesta pagina.
- Documentacao das atividades realizadas: paginas de Product API, Order API, Jenkins, MiniKube e Bottlenecks.
- Codigo fonte das atividades realizadas: `product-service/` e `order-service/`.
- Documentacao do projeto: arquitetura, execucao local e validacao.
- Codigo fonte do projeto: microservicos, Dockerfiles, Jenkinsfiles e manifests.
- Links para repositorios utilizados: repositorio individual e repositorio do projeto em grupo quando publicado.
- Bottlenecks implementados: cache Redis, HPA, metricas Prometheus e uso de banco relacional com Flyway.
- Apresentacao e video: pendentes de publicacao externa antes da entrega final.

## Observacao sobre Exchange API

A Order API consome a Exchange API quando a consulta de pedido usa o parametro `currency`. Para a entrega individual, o repositorio inclui um `support/mock-exchange` apenas para validacao local; a implementacao real da Exchange API fica no repositorio do outro integrante.

