# Uso de IA

O projeto permitiu uso parcial de IA para tarefas de apoio, desde que documentado.

## Uso registrado

IA foi utilizada como apoio para:

- revisar a organizacao da documentacao MkDocs;
- comparar a entrega com a rubrica publicada no site da disciplina;
- estruturar paginas de entrega individual;
- auxiliar na revisao textual de requisitos, pendencias e links;
- apoiar a separacao de commits e a validacao de comandos.

## Implementacao

O codigo das APIs individuais (`product-service` e `order-service`) foi mantido como contribuicao individual versionada neste repositorio. A IA foi usada como apoio de organizacao, verificacao e revisao, nao como substituta da explicacao do projeto.

## Responsabilidade

O aluno deve conseguir explicar:

- o fluxo `gateway-service -> product-service`;
- o fluxo `gateway-service -> order-service -> product-service`;
- a integracao `order-service -> exchange-service`;
- o uso de `id-account`;
- o cache Redis no Product Service;
- os testes automatizados das duas APIs;
- os manifests Kubernetes e Jenkinsfiles dos servicos.
