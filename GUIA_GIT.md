# Publicar e instalar pelo Git

Use um repositório **privado**, pois este projeto é comercial.

## 1. Publicar a partir da máquina de desenvolvimento

Instale o Git para Windows, abra o CMD e execute:

```cmd
cd /d C:Usersje_feDocumentsmercado
git init
git branch -M main
git config user.name "SEU NOME"
git config user.email "SEU EMAIL"
git add .
git status
git commit -m "Versao comercial inicial do Estoque Inteligente"
```

No GitHub, crie um repositório vazio e privado chamado `estoque-inteligente`. Não marque README, .gitignore ou licença, pois eles já existem.

Copie a URL HTTPS do repositório e execute:

```cmd
git remote add origin https://github.com/SEU-USUARIO/estoque-inteligente.git
git push -u origin main
```

O GitHub poderá solicitar login pelo navegador ou um token. O arquivo `.env`, senhas, banco e backups não serão enviados.

## 2. Instalar em outra máquina

Instale Git e Docker Desktop. No CMD:

```cmd
cd /d C:git clone https://github.com/SEU-USUARIO/estoque-inteligente.git EstoqueInteligente
cd /d C:EstoqueInteligente
copy .env.example .env
notepad .env
```

Configure no `.env` uma nova senha administrativa e um JWT secret exclusivo. Depois:

```cmd
docker compose up --build -d
docker compose ps
```

Abra `http://localhost:4200`.

## 3. Publicar atualizações

Na máquina de desenvolvimento:

```cmd
cd /d C:Usersje_feDocumentsmercado
git add .
git commit -m "Descreva a atualizacao"
git push
```

Na máquina do cliente, faça backup e atualize:

```cmd
cd /d C:EstoqueInteligente
docker compose exec -T postgres pg_dump -U estoque_user -d estoque_inteligente > backupsestoque_antes_atualizacao.sql
git pull
docker compose up --build -d
```

Nunca use `git reset --hard` ou `docker compose down -v` na máquina do cliente sem backup.
