# Jenkins

Cada microservico possui um `Jenkinsfile` proprio:

- `product-service/Jenkinsfile`
- `order-service/Jenkinsfile`

## Pipeline

As pipelines seguem o mesmo fluxo:

- checkout do codigo.
- download de dependencias Maven.
- build e testes com `./mvnw -B clean verify`.
- build de imagem Docker multi-plataforma.
- push da imagem para Docker Hub.
- deploy em Kubernetes usando `kubeconfig`.

## Credenciais esperadas no Jenkins

- `dockerhub-credential`: usuario e token do Docker Hub.
- `kubeconfig`: arquivo de acesso ao cluster Kubernetes.
- `DOCKERHUB_NAMESPACE`: namespace onde as imagens serao publicadas.

