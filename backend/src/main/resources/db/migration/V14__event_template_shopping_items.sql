ALTER TABLE event_template_items ALTER COLUMN product_id DROP NOT NULL;
ALTER TABLE event_template_items ADD COLUMN item_name VARCHAR(150);
ALTER TABLE event_template_items ADD COLUMN unit_of_measure VARCHAR(60);
ALTER TABLE event_template_items DROP CONSTRAINT IF EXISTS uk_event_template_item;
CREATE UNIQUE INDEX IF NOT EXISTS uk_event_template_item_product ON event_template_items(event_template_id, product_id) WHERE product_id IS NOT NULL;
UPDATE event_template_items eti SET item_name = p.name, unit_of_measure = p.unit_of_measure FROM products p WHERE eti.product_id = p.id AND eti.item_name IS NULL;
ALTER TABLE event_template_items ALTER COLUMN item_name SET NOT NULL;