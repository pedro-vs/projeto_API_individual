# CI/CD em EKS

Cada microservico possui um `Jenkinsfile` proprio. A pipeline padrao e:

- `SCM`
- `Dependencies`
- `Build`
- `Push to Docker Hub`
- `Deploy to K8s`

## Credenciais no Jenkins

Cadastre as seguintes credenciais:

| ID | Tipo | Uso |
| --- | --- | --- |
| `dockerhub-credential` | username/password | login e push no Docker Hub |
| `kubeconfig` | secret file | acesso ao cluster EKS |

O arquivo `kubeconfig` deve ser gerado depois do cluster EKS existir:

```bash
aws eks update-kubeconfig --region us-east-1 --name store-platform
```

## Variaveis no Jenkins

Configure as variaveis globais ou por job:

```bash
DOCKERHUB_NAMESPACE=<seu-usuario-dockerhub>
K8S_NAMESPACE=store-platform
USE_IN_CLUSTER_POSTGRES=false
DATABASE_HOST=<endpoint-rds>
DATABASE_PORT=5432
DATABASE_DB=store
DATABASE_USERNAME=store
DATABASE_PASSWORD=<senha>
USE_IN_CLUSTER_REDIS=true
```

O arquivo `infra/jenkins/jenkins-eks.env.example` documenta esses valores.

Para execucao local dos scripts, use `.env.example` como base:

```bash
cp .env.example .env
```

## Script comum de deploy

Os Jenkinsfiles chamam:

```bash
../scripts/ci/deploy-service.sh "$SERVICE_NAME" "${DOCKERHUB_NAMESPACE}/${SERVICE_NAME}:${BUILD_ID}"
```

Esse script:

- aplica o namespace;
- usa PostgreSQL externo em RDS por padrao;
- aplica Redis dentro do cluster por padrao;
- aplica `configmap`, `secret`, `deployment`, `service` e `hpa`;
- aguarda o rollout do deployment.

Para rodar no cluster local sem RDS:

```bash
export USE_IN_CLUSTER_POSTGRES=true
```
