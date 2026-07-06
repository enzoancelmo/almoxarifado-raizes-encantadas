ALTER TABLE products ADD COLUMN IF NOT EXISTS purchase_value DECIMAL(15,2) NOT NULL DEFAULT 0;
ALTER TABLE products ADD COLUMN IF NOT EXISTS exit_value DECIMAL(15,2) NOT NULL DEFAULT 0;

CREATE TABLE IF NOT EXISTS exit_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

INSERT INTO exit_types (name, description, active)
SELECT name, description, TRUE
FROM (VALUES
    ('Uso em cerimônia','Material utilizado em cerimônia ou ritual'),
    ('Uso interno da casa','Material utilizado internamente pelo Instituto'),
    ('Doação','Item separado para doação'),
    ('Perda/descarte','Item perdido, vencido, quebrado ou descartado'),
    ('Organização do almoxarifado','Ajuste por organização interna'),
    ('Ajuste após contagem','Correção após conferência física'),
    ('Outro','Outro motivo de saída')
) AS seed(name, description)
WHERE NOT EXISTS (SELECT 1 FROM exit_types e WHERE LOWER(e.name)=LOWER(seed.name));

ALTER TABLE stock_movements ADD COLUMN IF NOT EXISTS unit_value DECIMAL(15,2) NOT NULL DEFAULT 0;
ALTER TABLE stock_movements ADD COLUMN IF NOT EXISTS total_value DECIMAL(15,2) NOT NULL DEFAULT 0;
ALTER TABLE stock_movements ADD COLUMN IF NOT EXISTS exit_type_id BIGINT;
ALTER TABLE stock_movements ADD CONSTRAINT fk_stock_movements_exit_type
    FOREIGN KEY (exit_type_id) REFERENCES exit_types(id);
