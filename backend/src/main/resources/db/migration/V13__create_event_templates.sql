CREATE TABLE event_templates (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    event_type VARCHAR(120),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE event_template_items (
    id BIGSERIAL PRIMARY KEY,
    event_template_id BIGINT NOT NULL REFERENCES event_templates(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id),
    suggested_quantity INTEGER NOT NULL CHECK (suggested_quantity > 0),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_event_template_item UNIQUE (event_template_id, product_id)
);

ALTER TABLE stock_exit_batches ADD COLUMN event_template_id BIGINT REFERENCES event_templates(id);
ALTER TABLE stock_exit_batches ADD COLUMN total_different_items INTEGER NOT NULL DEFAULT 0;
ALTER TABLE stock_exit_batches ADD COLUMN total_quantity INTEGER NOT NULL DEFAULT 0;

CREATE INDEX idx_event_templates_active ON event_templates(active);
CREATE INDEX idx_event_template_items_template ON event_template_items(event_template_id);
CREATE INDEX idx_stock_exit_batches_template ON stock_exit_batches(event_template_id);