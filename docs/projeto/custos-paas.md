# Custos e PaaS

Esta pagina organiza a analise de custos para a entrega em AWS.

## Componentes considerados

| Componente | Servico AWS | Observacao |
| --- | --- | --- |
| Kubernetes gerenciado | Amazon EKS | custo do control plane por hora |
| Nodes do cluster | Amazon EC2 | custo por instancia e horas ligadas |
| Banco relacional | Amazon RDS PostgreSQL | custo por instancia, storage e backup |
| Disco dos nodes | EBS | volumes dos worker nodes |
| Rede | Load Balancer, trafego e NAT se usado | depende da topologia da VPC |
| Logs | CloudWatch | depende do volume de logs |

## Exemplo de calculo

Use a AWS Pricing Calculator com estes parametros base:

- Regiao: `us-east-1` ou a regiao usada pelo grupo.
- EKS: 1 cluster ligado durante o periodo de apresentacao/teste.
- EC2: 2 nodes `t3.medium`, com autoscaling ate 4 nodes.
- RDS PostgreSQL: 1 instancia pequena para ambiente academico.
- Storage RDS: 20 GiB.
- Load Balancer: 1 balanceador para expor o gateway, se usado.

Formula conceitual:

```text
custo_total =
  custo_eks_control_plane
  + custo_ec2_nodes
  + custo_rds_postgresql
  + custo_ebs
  + custo_load_balancer
  + custo_logs
  + custo_rede
```

## Onde usamos PaaS

O projeto usa PaaS/servicos gerenciados nos seguintes pontos:

- Amazon EKS para executar Kubernetes gerenciado.
- Amazon RDS PostgreSQL para banco relacional gerenciado.
- GitHub Pages para publicacao da documentacao MkDocs.
- Docker Hub como registry externo das imagens.

## Observacoes de economia

- Desligar o cluster quando nao estiver em uso.
- Remover Load Balancers depois da apresentacao.
- Usar instancias pequenas para ambiente academico.
- Evitar NAT Gateway se a arquitetura escolhida nao exigir subnets privadas com saida para internet.
- Conferir o AWS Billing Dashboard apos os testes.

