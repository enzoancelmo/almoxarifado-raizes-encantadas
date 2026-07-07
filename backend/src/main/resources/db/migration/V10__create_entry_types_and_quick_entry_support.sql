CREATE TABLE IF NOT EXISTS entry_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

INSERT INTO entry_types (name, description, active)
SELECT name, description, TRUE
FROM (VALUES
    ('Compra','Entrada por compra de material'),
    ('Doação','Entrada por doação recebida'),
    ('Devolução','Retorno de item ao almoxarifado'),
    ('Ajuste após contagem','Entrada gerada após conferência física'),
    ('Outro','Outra origem de entrada')
) AS seed(name, description)
WHERE NOT EXISTS (SELECT 1 FROM entry_types e WHERE LOWER(e.name)=LOWER(seed.name));

ALTER TABLE stock_movements ADD COLUMN IF NOT EXISTS entry_type_id BIGINT;
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_stock_movements_entry_type') THEN
        ALTER TABLE stock_movements ADD CONSTRAINT fk_stock_movements_entry_type FOREIGN KEY (entry_type_id) REFERENCES entry_types(id);
    END IF;
END $$;
