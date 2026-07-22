# Deploy em Nuvem — Almoxarifado Raízes Encantadas

Este guia coloca o sistema online para acesso fora da rede local.

Arquitetura recomendada:

```text
Frontend Angular: Vercel
Backend Spring Boot: Render Web Service
Banco PostgreSQL: Render PostgreSQL
```

## 1. Antes de começar

Confirme que o código está no GitHub:

```powershell
git status
git push -u origin main
```

O arquivo `.env` local não vai para o GitHub. Isso é correto.

## 2. Criar banco PostgreSQL na Render

1. Entre em `https://dashboard.render.com`.
2. Clique em **New +**.
3. Escolha **PostgreSQL**.
4. Crie o banco.
5. Guarde estes dados:
   - host;
   - database;
   - user;
   - password;
   - port.

Na Render, prefira usar o acesso interno entre serviços quando disponível.

## 3. Criar backend na Render

1. Clique em **New +**.
2. Escolha **Web Service**.
3. Conecte o repositório GitHub.
4. Configure:

```text
Name: almoxarifado-raizes-backend
Root Directory: backend
Environment: Docker
Branch: main
```

5. Em **Environment Variables**, cadastre:

```text
PORT=10000
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:postgresql://HOST_DO_RENDER:5432/NOME_DO_BANCO
DB_USERNAME=USUARIO_DO_BANCO
DB_PASSWORD=SENHA_DO_BANCO
JWT_SECRET=uma-chave-grande-com-pelo-menos-32-caracteres
JWT_EXPIRATION=86400000
ADMIN_NAME=Administrador
ADMIN_EMAIL=admin@raizesencantadas.com
ADMIN_PASSWORD=troque-esta-senha
CORS_ALLOWED_ORIGINS=https://SEU-FRONTEND.vercel.app
```

Importante:

- `JWT_SECRET` precisa ser uma chave forte.
- `ADMIN_PASSWORD` deve ser trocada antes de entregar ao cliente.
- Depois que o primeiro admin for criado, trocar `ADMIN_PASSWORD` não altera a senha já gravada no banco.

6. Faça deploy.
7. Teste:

```text
https://SEU-BACKEND.onrender.com/health
```

Deve retornar `UP`.

## 4. Criar frontend na Vercel

1. Entre em `https://vercel.com`.
2. Clique em **Add New Project**.
3. Importe o repositório GitHub.
4. Configure:

```text
Root Directory: frontend
Framework Preset: Angular
Build Command: npm run build:cloud
Output Directory: dist/estoque-inteligente/browser
Install Command: npm ci
```

5. Em **Environment Variables**, cadastre:

```text
FRONTEND_API_URL=https://SEU-BACKEND.onrender.com
```

6. Faça deploy.

Depois do deploy, copie a URL final da Vercel, por exemplo:

```text
https://almoxarifado-raizes.vercel.app
```

Volte na Render e ajuste:

```text
CORS_ALLOWED_ORIGINS=https://almoxarifado-raizes.vercel.app
```

Depois faça redeploy do backend.

## 5. Testes finais

Abra a URL da Vercel:

```text
https://SEU-FRONTEND.vercel.app
```

Teste:

- login;
- cadastro de item;
- entrada rápida;
- saída rápida;
- alertas;
- relatórios;
- exportação CSV;
- acesso pelo celular usando a mesma URL.

## 6. Começar com estoque zerado

As migrations antigas podem carregar dados iniciais de demonstração/importação. Para começar o cliente com estoque vazio, rode este comando **uma única vez** no banco PostgreSQL da Render, depois do primeiro deploy:

```sql
TRUNCATE TABLE event_template_items, stock_movements, products RESTART IDENTITY CASCADE;
```

Isso apaga:

- itens/produtos;
- movimentações;
- itens vinculados aos modelos de evento.

Isso preserva:

- usuário administrador;
- categorias;
- tipos de entrada/contribuição;
- tipos de saída.

Não rode esse comando depois que a cliente já estiver usando o sistema, porque ele apaga o histórico de estoque.

## 7. Alternativa: tudo na Render

Também é possível publicar frontend e backend na Render.

Use:

- PostgreSQL: Render PostgreSQL;
- Backend: Render Web Service com Docker e root `backend`;
- Frontend: Render Static Site com root `frontend`.

No frontend da Render:

```text
Build Command: npm run build:cloud
Publish Directory: dist/estoque-inteligente/browser
```

Variável:

```text
FRONTEND_API_URL=https://SEU-BACKEND.onrender.com
```

## 8. Atualização futura

Na sua máquina:

```powershell
git add .
git commit -m "Descricao da alteracao"
git push
```

Render e Vercel podem fazer deploy automático quando o GitHub receber atualização.

## 9. Custos e observações

- Planos gratuitos podem “dormir” depois de um tempo sem uso.
- O primeiro acesso depois de dormir pode demorar.
- Para cliente real, recomendo plano pago mínimo no backend/banco.
- Mantenha backup do banco.
- Nunca publique o arquivo `.env` real.
