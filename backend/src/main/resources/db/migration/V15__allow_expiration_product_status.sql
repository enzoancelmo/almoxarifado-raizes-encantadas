ALTER TABLE products DROP CONSTRAINT IF EXISTS products_status_check;

ALTER TABLE products ADD CONSTRAINT products_status_check
    CHECK (status IN (
        'NORMAL',
        'SALDO_NEGATIVO',
        'PENDENTE_CONTAGEM',
        'NECESSIDADE_REPOSICAO',
        'VENCENDO',
        'VENCIDO'
    ));
