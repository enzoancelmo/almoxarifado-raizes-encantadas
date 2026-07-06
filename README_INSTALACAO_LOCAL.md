# Instalação local — Almoxarifado Raízes Encantadas

Este modo instala PostgreSQL, backend e frontend no computador principal do Instituto. Até 15 pessoas na mesma rede podem acessar pelo navegador. O computador servidor precisa permanecer ligado e com o Docker Desktop aberto.

## 1. Preparar o computador servidor

1. Ative a virtualização na BIOS/UEFI.
2. Instale o [Docker Desktop para Windows](https://www.docker.com/products/docker-desktop/).
3. Quando solicitado, habilite o Subsistema do Windows para Linux (WSL 2) e reinicie o computador.
4. Instale o Git pelo PowerShell como administrador:

```powershell
winget install --id Git.Git -e --source winget
```

Feche e abra o PowerShell depois da instalação.

## 2. Baixar o projeto

No PowerShell:

```powershell
cd "$HOME\Documents"
git clone https://github.com/enzoancelmo/almoxarifado-raizes-encantadas.git
cd almoxarifado-raizes-encantadas
```

Se o repositório for privado, o GitHub solicitará autenticação. Use o navegador, GitHub Desktop ou um token; a senha normal da conta não funciona em operações Git.

## 3. Configurar as senhas

Crie o arquivo `.env`:

```powershell
Copy-Item .env.example .env
notepad .env
```

Troque `POSTGRES_PASSWORD`, `JWT_SECRET` e `ADMIN_PASSWORD`. A `JWT_SECRET` precisa ter pelo menos 32 caracteres; prefira uma chave aleatória de 64 caracteres. O `.env` é privado e não deve ser enviado ao GitHub.

## 4. Iniciar

Abra o Docker Desktop, aguarde aparecer como iniciado e dê dois cliques em `iniciar.bat`. Na primeira execução, o download e a compilação podem demorar.

Também é possível usar:

```powershell
docker compose up -d --build
```

Abra [http://localhost](http://localhost). O administrador inicial usa os valores `ADMIN_EMAIL` e `ADMIN_PASSWORD` do `.env`.

Verifique o estado:

```powershell
docker compose ps
docker compose logs -f
```

## 5. Acessar pelo celular ou outro computador

No servidor, descubra o IPv4:

```powershell
ipconfig
```

Procure `Endereço IPv4` do adaptador Wi-Fi ou Ethernet, por exemplo `192.168.0.50`. No aparelho conectado à mesma rede, abra:

```text
http://192.168.0.50
```

Não use `localhost` no celular. O roteador não pode estar com isolamento de clientes/rede de convidados ativado.

## 6. Liberar o Firewall do Windows

Abra o PowerShell como administrador e execute:

```powershell
New-NetFirewallRule -DisplayName "Almoxarifado Raízes Encantadas HTTP" -Direction Inbound -Protocol TCP -LocalPort 80 -Action Allow
```

Para remover essa regra futuramente:

```powershell
Remove-NetFirewallRule -DisplayName "Almoxarifado Raízes Encantadas HTTP"
```

Recomenda-se reservar um IP fixo para o servidor no roteador. Caso o IP mude, o endereço usado pelos celulares também muda.

## 7. Atalho e instalação no celular

- Android/Chrome: menu de três pontos → **Adicionar à tela inicial** ou **Instalar app**.
- iPhone/Safari: botão **Compartilhar** → **Adicionar à Tela de Início**.

Em rede HTTP local, o atalho funciona, mas navegadores podem bloquear a instalação PWA completa e o service worker por segurança. Para PWA completa fora de `localhost`, configure HTTPS com certificado confiável. Isso não impede o uso normal pelo navegador na rede local.

## 8. Usuários da equipe

Entre como `ADMIN` e crie usuários na área disponível no sistema. `ADMIN` pode cadastrar, editar e excluir cadastros. `USER` pode consultar e registrar movimentações, mas não altera ou exclui produtos, categorias e fornecedores. Cada pessoa deve usar sua própria conta.

## 9. Backup

Com o sistema iniciado, dê dois cliques em `backup.bat`. O arquivo será criado em `backups`, com data e hora:

```text
backups\backup_raizes_2026-07-06_14-30-00.sql
```

Copie periodicamente esses arquivos para outro disco ou pendrive.

## 10. Restaurar backup

Faça primeiro um backup dos dados atuais. Depois, arraste o arquivo `.sql` sobre `restaurar-backup.bat`, ou execute:

```powershell
.\restaurar-backup.bat ".\backups\backup_raizes_2026-07-06_14-30-00.sql"
```

Confirme a operação quando solicitado.

## 11. Parar, reiniciar e ver logs

- `parar.bat`: encerra os containers e preserva o banco.
- `reiniciar.bat`: recria e inicia o sistema.
- `logs.bat`: acompanha mensagens do sistema; pressione `Ctrl+C` para sair.

Nunca use `docker compose down -v` em operação normal: `-v` apaga o volume do banco.

## 12. Atualizar pelo GitHub

Faça backup e, na pasta do projeto, execute:

```powershell
git pull
.\reiniciar.bat
```

Alterações feitas no computador de desenvolvimento não chegam automaticamente aos demais clones: primeiro faça `git add`, `git commit` e `git push`; depois execute `git pull` e reinicie em cada servidor.

## Solução rápida de problemas

- **Docker diz que a virtualização está desativada:** confirme virtualização na BIOS, WSL 2 e recursos `Plataforma de Máquina Virtual` e `Subsistema do Windows para Linux`.
- **Porta 80 ocupada:** execute `netstat -ano | findstr :80` e identifique o programa antes de encerrá-lo.
- **Celular não acessa:** confirme mesmo Wi-Fi, IPv4 correto, firewall liberado e rede de convidados desativada.
- **Tela não abre:** execute `docker compose ps`; todos os serviços devem estar em execução/saudáveis.
- **Banco não inicia após trocar credenciais:** credenciais de um volume já criado não mudam automaticamente. Restaure um backup em instalação limpa ou mantenha as credenciais originais.
