# AWS

Esta pagina registra a preparacao da conta AWS para executar o projeto em EKS.

## O que precisa existir na conta

- Usuario IAM com acesso programatico.
- Access key e secret access key geradas para esse usuario.
- AWS CLI instalada na maquina local e/ou no agente Jenkins.
- Regiao padrao definida.
- Permissoes para criar ou acessar EKS, EC2, IAM, VPC, CloudFormation e RDS.

## Configuracao local

Instale a AWS CLI e configure as credenciais:

```bash
aws configure
```

Valores esperados:

```text
AWS Access Key ID: <access-key>
AWS Secret Access Key: <secret-key>
Default region name: us-east-1
Default output format: json
```

Depois valide:

```bash
cp .env.example .env
# edite o .env com AWS_REGION e EKS_CLUSTER_NAME
./scripts/aws/check-aws-prereqs.sh
```

O script confere:

- `aws`
- `kubectl`
- `docker`
- identidade AWS com `aws sts get-caller-identity`
- regiao configurada

## Arquivos adicionados

- `scripts/aws/check-aws-prereqs.sh`
- `scripts/aws/create-eks-cluster.sh`
- `scripts/aws/update-kubeconfig.sh`
- `.env.example`
- `infra/aws/rds-postgres.env.example`

## Seguranca

As credenciais AWS nao devem ser commitadas. Use variaveis de ambiente, o arquivo de credenciais da AWS CLI ou credenciais protegidas no Jenkins.
