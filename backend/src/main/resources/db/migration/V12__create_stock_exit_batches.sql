CREATE TABLE stock_exit_batches (
    id BIGSERIAL PRIMARY KEY,
    event_name VARCHAR(180) NOT NULL,
    exit_type_id BIGINT REFERENCES exit_types(id),
    responsible_name VARCHAR(150),
    exit_date DATE,
    notes TEXT,
    total_value NUMERIC(15,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE stock_movements ADD COLUMN exit_batch_id BIGINT REFERENCES stock_exit_batches(id);
CREATE INDEX idx_stock_movements_exit_batch ON stock_movements(exit_batch_id);
CREATE INDEX idx_stock_exit_batches_event ON stock_exit_batches(LOWER(event_name));