# Roteiro de apresentação — Almoxarifado Raízes Encantadas

Este roteiro ajuda a apresentar o sistema ao Instituto de forma simples, sem entrar em detalhes técnicos.

## Objetivo da apresentação

Mostrar como o sistema substitui a planilha e facilita:

- a conferência dos itens;
- o acompanhamento das quantidades;
- a identificação do que precisa de reposição;
- o registro de entradas, saídas e ajustes;
- a consulta pelo computador ou celular;
- a segurança das informações por meio de usuários e backup.

## Antes da reunião

1. Ligue o computador servidor.
2. Abra o Docker Desktop e aguarde a inicialização.
3. Na pasta do projeto, execute `iniciar.bat`.
4. Abra `http://localhost`.
5. Confirme que consegue entrar no sistema.
6. Verifique as telas Itens, Dashboard, Alertas e Movimentações.
7. Deixe um celular conectado ao mesmo Wi-Fi para demonstrar o acesso móvel.
8. Faça um backup antes de alterar dados durante a demonstração.

Login inicial:

```text
E-mail: admin@raizesencantadas.com
Senha: admin123
```

Troque essa senha antes de colocar o sistema em uso definitivo.

## Abertura sugerida

Você pode começar dizendo:

> “Este sistema foi criado a partir da planilha atual do Instituto. Os 123 itens foram preservados e organizados em um almoxarifado digital, que pode ser acessado pelo computador e pelos celulares conectados à rede do Instituto.”

Explique que o objetivo não é complicar a rotina, mas tornar a conferência e a reposição mais claras.

## Sequência recomendada da demonstração

### 1. Login

Mostre que o acesso é restrito a pessoas autorizadas.

Explique:

- cada pessoa pode ter seu próprio usuário;
- o administrador pode criar usuários;
- as informações ficam protegidas por senha;
- é possível sair do sistema ao terminar o uso.

### 2. Dashboard

Apresente o Dashboard como um resumo rápido do almoxarifado.

Destaque:

- total de itens cadastrados;
- itens pendentes de contagem;
- itens com saldo negativo;
- itens que precisam de reposição;
- movimentações recentes.

Frase sugerida:

> “Em vez de procurar linha por linha na planilha, esta tela mostra rapidamente o que precisa de atenção.”

### 3. Lista de itens

Abra a tela **Itens** e mostre:

- os 123 itens importados;
- nome e marca;
- entidade ou finalidade;
- tipo;
- unidade de medida;
- quantidade atual;
- necessidade mensal;
- saldo;
- status.

Demonstre os filtros por nome, tipo, status e finalidade.

Explique que os itens sem quantidade preenchida na planilha foram marcados como **Pendente de contagem**. Eles não foram considerados automaticamente como estoque real zerado.

### 4. Explicação do saldo

Use uma explicação simples:

```text
Saldo = Quantidade atual − Necessidade mensal
```

Exemplo do Dendê:

```text
Quantidade atual: 3 litros
Necessidade mensal: 5 litros
Saldo: −2 litros
```

Isso significa:

> “Existem 3 litros disponíveis, mas a necessidade prevista é de 5 litros. Portanto, faltam 2 litros.”

Outros exemplos:

- saldo `5`: existem 5 unidades além da necessidade mensal;
- saldo `0`: a quantidade atual atende exatamente à necessidade;
- saldo `−3`: faltam 3 unidades;
- pendente de contagem: a quantidade ainda precisa ser conferida e o saldo não deve ser considerado definitivo.

Pergunte à cliente se prefere visualizar a coluna como **Saldo**, **Diferença mensal** ou diretamente como **Faltam/Sobram**.

### 5. Cadastro e edição

Cadastre um item de teste ou edite um item previamente preparado.

Mostre os campos:

- item;
- marca;
- entidade/finalidade;
- tipo;
- unidade;
- quantidade atual;
- necessidade mensal;
- observações.

Explique que o saldo e o status são calculados automaticamente.

### 6. Movimentações

Registre uma movimentação de exemplo.

Tipos disponíveis:

- **Entrada:** soma à quantidade atual;
- **Saída:** reduz a quantidade atual;
- **Ajuste:** define a quantidade encontrada durante uma contagem.

Mostre também:

- responsável;
- finalidade;
- cerimônia ou evento;
- motivo;
- observações;
- quantidade anterior e nova quantidade.

Exemplo:

> “Se chegarem 3 litros de Dendê por compra ou doação, registramos uma entrada. A quantidade e o saldo são atualizados automaticamente.”

O sistema não permite uma saída maior que a quantidade disponível.

### 7. Alertas e reposição

Abra a tela **Alertas** e explique:

- **Pendente de contagem:** ainda precisa de conferência física;
- **Saldo negativo:** a quantidade é menor que a necessidade mensal;
- **Necessidade de reposição:** não há quantidade disponível para uma necessidade já definida;
- **Normal:** não existe pendência naquele momento.

Depois mostre a lista de reposição nos relatórios.

### 8. Sugestões e mensagens internas

Mostre que o sistema gera orientações simples, por exemplo:

- conferir itens sem contagem;
- repor um item com saldo negativo;
- avisar os responsáveis antes de uma cerimônia.

Na tela de mensagens internas, demonstre como copiar um texto e enviá-lo pelo WhatsApp. O sistema apenas prepara a mensagem; ele não envia automaticamente.

### 9. Acesso pelo celular

No computador servidor, execute:

```powershell
ipconfig
```

Use o endereço IPv4 do Wi-Fi. Exemplo:

```text
http://192.168.4.30
```

Abra esse endereço no celular conectado ao mesmo Wi-Fi.

Explique:

- o computador servidor precisa permanecer ligado;
- os celulares precisam estar na mesma rede;
- o sistema pode ser adicionado à tela inicial como atalho;
- é recomendável manter um IP fixo para o servidor.

### 10. Backup

Mostre o arquivo `backup.bat`.

Explique:

> “O backup cria uma cópia do banco de dados com data e hora. Essa cópia deve ser guardada também em outro computador, disco ou pendrive.”

Não é necessário executar uma restauração durante a apresentação. Apenas explique que existe um procedimento documentado e que uma restauração substitui os dados atuais.

## Perguntas para fazer à cliente

Ao final, confirme:

1. O termo **necessidade mensal** representa corretamente a realidade do Instituto?
2. O saldo deve aparecer como número ou como “faltam/sobram”?
3. Quais pessoas serão responsáveis pelas contagens?
4. Quem poderá cadastrar e excluir itens?
5. Todos os usuários poderão registrar movimentações?
6. Quais unidades de medida são utilizadas?
7. A finalidade deve aceitar texto livre ou opções cadastradas?
8. O campo cerimônia/evento será usado com frequência?
9. Com qual periodicidade será feita a conferência?
10. Quem ficará responsável pelos backups?

Anote as respostas como ajustes de homologação. Evite prometer alterações no momento sem antes avaliar impacto e prazo.

## Encerramento sugerido

> “A primeira etapa é conferir as quantidades dos itens marcados como pendentes. Depois dessa contagem inicial, o Instituto passa a registrar todas as entradas, saídas e ajustes no sistema. Assim, o Dashboard e a lista de reposição ficam cada vez mais confiáveis.”

Combine uma rotina inicial:

1. cadastrar os usuários;
2. realizar a primeira contagem;
3. revisar as necessidades mensais;
4. começar a registrar movimentações;
5. fazer backups periódicos;
6. marcar uma revisão após alguns dias de uso.

## Checklist de aceite

Antes de considerar a entrega concluída, valide com a cliente:

- [ ] Login funcionando;
- [ ] nome e identidade do Instituto corretos;
- [ ] 123 itens disponíveis;
- [ ] categorias corretas;
- [ ] Dendê com 3 litros, necessidade 5 e saldo −2;
- [ ] cadastro e edição de item;
- [ ] entrada, saída e ajuste;
- [ ] alertas compreensíveis;
- [ ] lista de reposição;
- [ ] acesso pelo celular;
- [ ] criação dos usuários;
- [ ] backup realizado;
- [ ] responsável pelo servidor definido;
- [ ] responsável pelas contagens definido.

## Se algo não abrir durante a apresentação

Na pasta do projeto, execute:

```powershell
docker compose ps
```

Os serviços devem aparecer como `running` ou `healthy`.

Para reiniciar:

```powershell
.\reiniciar.bat
```

Para consultar mensagens do sistema:

```powershell
.\logs.bat
```
