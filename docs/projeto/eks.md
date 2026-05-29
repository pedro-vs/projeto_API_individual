# EKS

Esta pagina descreve como criar e conectar o cluster EKS usado no projeto em grupo.

## Opcoes de criacao

O handout pede a criacao da infraestrutura AWS para rodar Kubernetes. O repositorio deixa dois caminhos documentados:

- criar manualmente pelo console AWS seguindo o handout;
- criar por `eksctl` usando o template versionado em `infra/eks/eksctl-cluster.yaml.example`.

## Criacao com `eksctl`

Copie o template e ajuste regiao, nome e tamanho dos nodes:

```bash
cp infra/eks/eksctl-cluster.yaml.example infra/eks/eksctl-cluster.yaml
```

Crie o cluster:

```bash
./scripts/aws/create-eks-cluster.sh
```

Atualize o `kubeconfig`:

```bash
cp .env.example .env
# edite AWS_REGION e EKS_CLUSTER_NAME no .env
./scripts/aws/update-kubeconfig.sh
```

Valide:

```bash
kubectl get nodes
kubectl get ns
```

## Banco de dados

Para a entrega em AWS, a recomendacao do projeto e usar PostgreSQL gerenciado em Amazon RDS.

As APIs que precisam de banco sao:

- `account-service`
- `product-service`
- `order-service`

As variaveis usadas por esses servicos sao:

```bash
DATABASE_HOST=<endpoint-rds>
DATABASE_PORT=5432
DATABASE_DB=store
DATABASE_USERNAME=store
DATABASE_PASSWORD=<senha>
```

O arquivo `infra/aws/rds-postgres.env.example` mostra o formato esperado.

## Deploy no EKS

Com as imagens ja publicadas no Docker Hub:

```bash
cp .env.example .env
# edite DOCKERHUB_NAMESPACE, IMAGE_TAG e as variaveis DATABASE_* no .env
./scripts/aws/deploy-eks.sh
```

Por padrao, esse deploy usa RDS para PostgreSQL e Redis dentro do cluster.

Se precisar usar PostgreSQL dentro do cluster, apenas para teste:

```bash
export USE_IN_CLUSTER_POSTGRES=true
./scripts/aws/deploy-eks.sh
```
