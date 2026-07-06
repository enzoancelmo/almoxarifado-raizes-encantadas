ALTER TABLE products ADD COLUMN IF NOT EXISTS average_cost DECIMAL(15,2) NOT NULL DEFAULT 0;
ALTER TABLE products ADD COLUMN IF NOT EXISTS current_stock_value DECIMAL(15,2) NOT NULL DEFAULT 0;

UPDATE products
SET current_stock_value = COALESCE(NULLIF(current_stock_value, 0), quantity * COALESCE(purchase_value, 0)),
    average_cost = CASE
        WHEN quantity > 0 THEN ROUND((COALESCE(NULLIF(current_stock_value, 0), quantity * COALESCE(purchase_value, 0)) / quantity)::numeric, 2)
        ELSE 0
    END;
