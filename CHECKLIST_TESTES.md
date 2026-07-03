# Checklist de Testes — Estoque Inteligente

## Autenticação
- [ ] Login com admin
- [ ] Token gerado
- [ ] Logout
- [ ] Acesso negado sem token
- [ ] Sessão expirada redireciona ao login

## Produtos
- [ ] Criar produto
- [ ] Editar produto
- [ ] Excluir produto
- [ ] Filtrar por nome, categoria e status
- [ ] Validar estoque baixo, vencendo e vencido

## Categorias
- [ ] Criar categoria
- [ ] Editar categoria
- [ ] Excluir categoria sem produtos
- [ ] Bloquear exclusão de categoria em uso

## Fornecedores
- [ ] Criar fornecedor
- [ ] Editar fornecedor
- [ ] Excluir fornecedor
- [ ] Vincular e remover fornecedor do produto

## Movimentações
- [ ] Entrada soma estoque
- [ ] Saída reduz estoque
- [ ] Bloquear saída maior que estoque
- [ ] Ajuste define quantidade final
- [ ] Histórico mais recente primeiro
- [ ] Status recalculado

## Dashboard e alertas
- [ ] Cards carregando dados reais
- [ ] Últimas movimentações
- [ ] Estoque baixo
- [ ] Vencendo
- [ ] Vencido

## Relatórios
- [ ] Estoque geral
- [ ] Sugestão de compra
- [ ] Copiar lista de compra
- [ ] Sugestões inteligentes por prioridade

## Mensagens WhatsApp
- [ ] Gerar mensagens
- [ ] Copiar mensagens

## Responsividade
- [ ] Desktop
- [ ] Tablet
- [ ] Celular

## Deploy
- [ ] `docker compose up --build -d`
- [ ] Health check público
- [ ] Login e APIs protegidas
- [ ] Persistência após reiniciar contêineres
