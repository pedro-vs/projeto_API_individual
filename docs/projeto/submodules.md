# Git Submodules

O handout do projeto em grupo pode cobrar que as implementacoes individuais sejam incorporadas ao repositorio principal como Git submodules.

## Situacao atual

Neste repositorio, os microservicos estao versionados como pastas normais:

- `product-service`
- `order-service`
- `exchange-service`

Isso facilita a validacao local, mas nao e a mesma coisa que submodule.

## Como converter quando os repos individuais existirem

Depois que cada membro publicar seu repositorio individual, defina as URLs:

```bash
export PRODUCT_REPO_URL=https://github.com/<org-ou-user>/<product-repo>.git
export ORDER_REPO_URL=https://github.com/<org-ou-user>/<order-repo>.git
export EXCHANGE_REPO_URL=https://github.com/<org-ou-user>/<exchange-repo>.git
```

Como as pastas ja existem, primeiro mova ou remova as pastas versionadas no repositorio do grupo. Depois rode:

```bash
./scripts/git/add-individual-submodules.sh
```

O script executa:

```bash
git submodule add "$PRODUCT_REPO_URL" product-service
git submodule add "$ORDER_REPO_URL" order-service
git submodule add "$EXCHANGE_REPO_URL" exchange-service
git submodule update --init --recursive
```

## Comandos uteis

Clonar o repo do grupo com submodules:

```bash
git clone --recurse-submodules <repo-do-grupo>
```

Atualizar submodules:

```bash
git submodule update --init --recursive
```

