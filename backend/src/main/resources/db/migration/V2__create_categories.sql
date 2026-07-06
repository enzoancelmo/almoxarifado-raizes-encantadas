CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO categories (name, description) VALUES
    ('Alimentos', 'Produtos alimentícios em geral'),
    ('Bebidas', 'Bebidas alcoólicas e não alcoólicas'),
    ('Limpeza', 'Produtos para limpeza'),
    ('Higiene', 'Produtos de higiene pessoal'),
    ('Frios', 'Frios, laticínios e refrigerados'),
    ('Hortifruti', 'Frutas, verduras e legumes'),
    ('Outros', 'Produtos não classificados');
