# Almoxarifado Raízes Encantadas

Sistema web local para gestão de itens, materiais, medicinas e recursos de uso espiritual do Instituto Raízes Encantadas.

## Funcionalidades

- 123 itens importados da planilha `Almoxarifado - Itens Instituto.xlsx`
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

Para demonstrar o sistema ao Instituto, siga o [README_APRESENTACAO_CLIENTE.md](README_APRESENTACAO_CLIENTE.md).

## Carga inicial

A migration `V7__instituto_initial_data.sql` cria 12 categorias e 123 itens. Campos vazios de quantidade atual são carregados como zero com status `PENDENTE_CONTAGEM`. O Dendê é carregado com 3 litros, necessidade mensal 5 e saldo calculado em −2.

## Tecnologias

Java 17, Spring Boot, Angular, PostgreSQL, JWT, Flyway, Docker Compose e Nginx.
