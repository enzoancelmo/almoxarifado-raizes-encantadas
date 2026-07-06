# Estoque Inteligente

> Para instalar como servidor em outro PC e acessar pela rede local, consulte [README_INSTALACAO_LOCAL.md](README_INSTALACAO_LOCAL.md).

**Controle de estoque, validade e reposição para pequenos mercados.**

MVP profissional voltado a mercados, mercearias, adegas, lojas de conveniência e comércios locais que desejam substituir papel, planilhas e controles dispersos.

## Funcionalidades

- Login seguro com JWT e senha BCrypt
- Produtos, categorias e fornecedores
- Entrada, saída e ajuste manual de estoque
- Cálculo automático de estoque baixo e validade
- Dashboard com indicadores reais
- Alertas de estoque e vencimento
- Relatórios e sugestão de compra
- Sugestões comerciais baseadas em regras
- Mensagens promocionais prontas para WhatsApp
- Interface responsiva para desktop e celular

## Tecnologias

- Java 17, Spring Boot 3.5, Spring Security, JPA, Validation e Flyway
- Angular 20 standalone
- PostgreSQL 17
- Docker e Docker Compose

## Estrutura

- `backend/`: API REST e regras de negócio
- `frontend/`: aplicação Angular
- `docker-compose.yml`: PostgreSQL, backend e frontend
- `PROPOSTA_COMERCIAL.md`: material de apresentação
- `CHECKLIST_TESTES.md`: roteiro de homologação

## Acesso inicial

- E-mail: valor de `ADMIN_EMAIL` no arquivo `.env`
- Senha: valor de `ADMIN_PASSWORD` no arquivo `.env`

O usuário é criado automaticamente com senha BCrypt no primeiro startup.

> Para instalar em outro computador Windows, consulte [README_INSTALACAO_CLIENTE.md](README_INSTALACAO_CLIENTE.md).

## Execução local

Pré-requisitos: Java 17 ou superior, Maven, Node.js compatível com Angular 20, npm e Docker.

### Banco

```bash
docker compose up -d postgres
```

### Backend

```bash
cd backend
mvn clean test
mvn spring-boot:run
```

API: `http://localhost:8080`. Health check: `GET /health`.

### Frontend

```bash
cd frontend
npm install
npm start
```

Aplicação: `http://localhost:4200`.

## Execução completa com Docker

Primeiro copie `.env.example` para `.env` e troque as senhas.

```bash
docker compose up --build -d
```

- Sistema: `http://localhost`
- Pela rede local: `http://IP-DO-SERVIDOR`
- API: caminho relativo `/api`, encaminhado internamente pelo Nginx
- Backend e PostgreSQL não ficam expostos à rede

Para encerrar: `docker compose down`. Para remover também os dados locais: `docker compose down -v`.

## Variáveis de ambiente

| Variável | Uso |
|---|---|
| `DB_URL` | Conexão JDBC do backend |
| `DB_USERNAME` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |
| `JWT_SECRET` | Chave de assinatura JWT; obrigatória e exclusiva em produção |
| `JWT_EXPIRATION` | Duração do token em milissegundos |
| `SERVER_PORT` | Porta do backend |
| `ADMIN_NAME`, `ADMIN_EMAIL`, `ADMIN_PASSWORD` | Conta administrativa criada no primeiro startup |

Os valores padrão são exclusivamente para desenvolvimento local.

## Endpoints principais

| Área | Endpoints |
|---|---|
| Autenticação | `POST /auth/login`, `POST /auth/register` |
| Categorias | `GET/POST /categories`, `GET/PUT/DELETE /categories/{id}` |
| Fornecedores | `GET/POST /suppliers`, `GET/PUT/DELETE /suppliers/{id}` |
| Produtos | `GET/POST /products`, `GET/PUT/DELETE /products/{id}` |
| Estoque | `GET/POST /stock-movements`, histórico por produto |
| Dashboard | `GET /dashboard/summary` |
| Alertas | `GET /alerts/low-stock`, `/expiring`, `/expired`, `/summary` |
| Relatórios | `GET /reports/stock`, `/low-stock`, `/expiring`, `/expired`, `/purchase-suggestion` |
| Sugestões | `GET /smart-suggestions` |
| WhatsApp | `GET /whatsapp-messages` |

Exceto login e health check, as APIs exigem `Authorization: Bearer TOKEN`. O registro exige token de administrador.

## Exemplo de API

```bash
curl -X POST http://localhost/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"SEU_ADMIN_EMAIL","password":"SUA_SENHA"}'

curl http://localhost/api/products \
  -H "Authorization: Bearer SEU_TOKEN"
```

## Telas

- Login com autenticação real
- Dashboard gerencial
- Produtos com busca e filtros
- Categorias e fornecedores
- Histórico e formulário de movimentações
- Alertas segmentados
- Relatórios com lista de compra copiável
- Sugestões inteligentes e mensagens WhatsApp

A migration `V7` inclui dados de demonstração para todos os cenários relevantes.

## Observações de segurança

Antes de usar, copie `.env.example` para `.env`, troque `POSTGRES_PASSWORD`, `JWT_SECRET` e `ADMIN_PASSWORD` e nunca versione o arquivo `.env`. O sistema é privado: `/auth/register` exige uma sessão `ADMIN` e cria somente usuários com perfil `USER`. O MVP não inclui refresh token nem recuperação de senha.

## Próximos passos

- Multiempresa e perfis detalhados
- Paginação e pesquisa no backend
- Importação por planilha e código de barras
- Rotinas automáticas de backup
- Testes de integração com Testcontainers
- Deploy com domínio, TLS e observabilidade
