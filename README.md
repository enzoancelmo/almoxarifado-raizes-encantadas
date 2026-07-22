# Almoxarifado Raízes Encantadas

Sistema web local para gestão de itens, materiais, medicinas e recursos de uso espiritual do Instituto Raízes Encantadas.

## Funcionalidades

- Cadastro de itens, materiais, medicinas e recursos do Instituto
- Tipos, marcas, finalidades e unidades preservados
- Quantidade atual, necessidade mensal, saldo e status automáticos
- Pendências de contagem e lista de reposição
- Entradas, saídas e ajustes com histórico
- Dashboard, alertas, relatórios e mensagens internas
- Login JWT com perfis `ADMIN` e `USER`
- Acesso pelo celular na mesma rede, PWA e backup local

## Executar

```powershell
Copy-Item .env.example .env
notepad .env
.\iniciar.bat
```

Acesse `http://localhost`. O login inicial é `admin@raizesencantadas.com` / `admin123`; altere a senha antes do uso real.

No celular, use `http://IP-DO-SERVIDOR`. Consulte [README_INSTALACAO_LOCAL.md](README_INSTALACAO_LOCAL.md).

Para publicar em nuvem com Vercel/Render, consulte [README_DEPLOY_NUVEM.md](README_DEPLOY_NUVEM.md).

Para demonstrar o sistema ao Instituto, siga o [README_APRESENTACAO_CLIENTE.md](README_APRESENTACAO_CLIENTE.md).

## Carga inicial

O sistema cria categorias e dados estruturais para iniciar o uso. Para deploy comercial com estoque vazio, siga o passo "Começar com estoque zerado" em [README_DEPLOY_NUVEM.md](README_DEPLOY_NUVEM.md).

## Tecnologias

Java 17, Spring Boot, Angular, PostgreSQL, JWT, Flyway, Docker Compose e Nginx.

## Controle financeiro do almoxarifado

Esta etapa adiciona valor estimado de estoque, entradas, saídas e gasto por evento. Não é controle de caixa real; é valorização dos materiais do almoxarifado.

- Valor estimado em estoque = quantidade atual x valor de compra do item.
- Valor total da entrada = quantidade x valor unitário da entrada.
- Valor total da saída = quantidade x valor unitário da saída.
- Gasto por evento = soma das saídas vinculadas ao evento/cerimônia.

### Telas

- `Itens`: cadastro com `Valor de compra`, `Valor de saída` e valor total em estoque.
- `Movimentações`: entrada/saída/ajuste com valor unitário, valor total, responsável e evento.
- `Tipos de Saída`: cadastro de motivos estruturados de saída.
- `Valores`: cards financeiros e últimas movimentações com valor.
- `Relatórios > Por evento`: soma dos materiais usados em uma cerimônia/evento.

### Endpoints financeiros

- `GET /api/exit-types`
- `POST /api/exit-types`
- `PUT /api/exit-types/{id}`
- `DELETE /api/exit-types/{id}`
- `GET /api/financial-reports/summary`
- `GET /api/financial-reports/event-costs?eventName=Nome do evento`
- `GET /api/exports/items.csv`
- `GET /api/exports/movements.csv`
- `GET /api/exports/event-costs.csv`
- `GET /api/exports/financial-summary.csv`

### Como testar pela interface

1. Acesse `http://localhost`.
2. Entre com `admin@raizesencantadas.com` / `admin123`.
3. Vá em `Itens`, edite um item e preencha os valores.
4. Vá em `Tipos de Saída` e cadastre ou confirme as opções.
5. Vá em `Movimentações > Nova Movimentação`.
6. Registre uma entrada ou saída com valor unitário.
7. Em saída, escolha o tipo de saída e informe o evento/cerimônia.
8. Abra `Valores` para conferir os totais.
9. Abra `Relatórios > Por evento` para consultar o gasto por cerimônia.
10. Use os botões de exportação para baixar CSV.
