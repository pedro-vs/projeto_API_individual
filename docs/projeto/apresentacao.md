# Apresentacao

Esta pagina serve como roteiro para os slides e para o video final.

## Estrutura sugerida

1. Contexto do projeto.
2. Arquitetura de microservicos.
3. APIs individuais integradas no projeto em grupo.
4. Deploy em AWS/EKS.
5. CI/CD com Jenkins.
6. Bottlenecks e melhorias.
7. Teste de carga e HPA.
8. Custos e PaaS.
9. Conclusao.

## Demonstracao recomendada

No video, mostrar:

- repositorio e documentacao publicada;
- `kubectl get pods,svc,hpa`;
- gateway respondendo;
- pipeline Jenkins publicando imagem e fazendo deploy;
- teste de carga com `k6`;
- HPA aumentando replicas.

## Roteiro curto para narracao

```text
Este projeto implementa uma plataforma de loja em microservicos.
Temos servicos de conta, autenticacao, gateway, produtos, pedidos e cambio.
As APIs individuais foram integradas no projeto em grupo e publicadas em Kubernetes.
O deploy em AWS usa EKS, imagens no Docker Hub e PostgreSQL gerenciado por RDS.
O Jenkins executa build, testes, push de imagem e deploy no cluster.
Para tratar bottlenecks, usamos cache com Redis e observabilidade com Prometheus e Grafana.
Por fim, executamos teste de carga com k6 e observamos o HPA escalando os servicos.
```

## Pendencias externas

- Link dos slides.
- Link do video publicado.
- Prints ou gravacao do HPA escalando.

