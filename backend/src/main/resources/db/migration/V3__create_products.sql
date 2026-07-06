CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    barcode VARCHAR(50) UNIQUE,
    category_id BIGINT NOT NULL REFERENCES categories(id),
    cost_price NUMERIC(12, 2) NOT NULL CHECK (cost_price >= 0),
    sale_price NUMERIC(12, 2) NOT NULL CHECK (sale_price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    minimum_stock INTEGER NOT NULL CHECK (minimum_stock >= 0),
    expiration_date DATE,
    status VARCHAR(20) NOT NULL CHECK (status IN ('NORMAL', 'ESTOQUE_BAIXO', 'VENCENDO', 'VENCIDO')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_name ON products (LOWER(name));
CREATE INDEX idx_products_status ON products (status);
CREATE INDEX idx_products_category ON products (category_id);

INSERT INTO products (name, barcode, category_id, cost_price, sale_price, quantity, minimum_stock, expiration_date, status) VALUES
('Arroz 5kg', '7891000000011', (SELECT id FROM categories WHERE name='Alimentos'), 22.50, 29.90, 25, 8, CURRENT_DATE + 180, 'NORMAL'),
('Feijão 1kg', '7891000000028', (SELECT id FROM categories WHERE name='Alimentos'), 5.40, 7.99, 6, 10, CURRENT_DATE + 120, 'ESTOQUE_BAIXO'),
('Leite Integral', '7891000000035', (SELECT id FROM categories WHERE name='Frios'), 3.80, 5.49, 18, 8, CURRENT_DATE + 12, 'VENCENDO'),
('Detergente', '7891000000042', (SELECT id FROM categories WHERE name='Limpeza'), 1.65, 2.79, 35, 10, NULL, 'NORMAL'),
('Café 500g', '7891000000059', (SELECT id FROM categories WHERE name='Alimentos'), 14.00, 19.90, 15, 5, CURRENT_DATE + 240, 'NORMAL');
