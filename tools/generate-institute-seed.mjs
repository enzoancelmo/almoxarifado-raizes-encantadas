import fs from "node:fs/promises";
import { FileBlob, SpreadsheetFile } from "@oai/artifact-tool";

const [inputPath, outputPath] = process.argv.slice(2);
if (!inputPath || !outputPath) {
  throw new Error("Uso: node generate-institute-seed.mjs <planilha.xlsx> <migration.sql>");
}

const workbook = await SpreadsheetFile.importXlsx(await FileBlob.load(inputPath));
const rows = workbook.worksheets.getItem("Planilha3").getRange("A2:H124").values;
const escapeSql = value => String(value ?? "").trim().replaceAll("'", "''");
const categories = [...new Set(rows.map(row => String(row[6] ?? "").trim()).filter(Boolean))];

const values = rows.map(row => {
  const current = row[4] == null ? 0 : Number(row[4]);
  const monthly = row[5] == null ? 0 : Number(row[5]);
  const pending = row[4] == null;
  const status = pending
    ? "PENDENTE_CONTAGEM"
    : current === 0 && monthly > 0
      ? "NECESSIDADE_REPOSICAO"
      : monthly > 0 && current < monthly
        ? "SALDO_NEGATIVO"
        : "NORMAL";
  const nullable = value => escapeSql(value) ? `'${escapeSql(value)}'` : "NULL";
  return `('${escapeSql(row[0])}', ${nullable(row[1])}, ${nullable(row[2])}, ${nullable(row[3])}, ` +
    `(SELECT id FROM categories WHERE name='${escapeSql(row[6])}'), 0, 0, ${current}, ${monthly}, ${monthly}, ` +
    `${pending}, '${status}', NULL)`;
});

const sql = `ALTER TABLE products DROP CONSTRAINT IF EXISTS products_status_check;
ALTER TABLE products ADD COLUMN brand VARCHAR(150);
ALTER TABLE products ADD COLUMN entity_purpose VARCHAR(500);
ALTER TABLE products ADD COLUMN unit_of_measure VARCHAR(60);
ALTER TABLE products ADD COLUMN monthly_required_quantity INTEGER NOT NULL DEFAULT 0 CHECK (monthly_required_quantity >= 0);
ALTER TABLE products ADD COLUMN count_pending BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE products ADD COLUMN notes TEXT;
ALTER TABLE stock_movements ADD COLUMN responsible_name VARCHAR(150);
ALTER TABLE stock_movements ADD COLUMN purpose VARCHAR(255);
ALTER TABLE stock_movements ADD COLUMN event_name VARCHAR(180);
ALTER TABLE stock_movements ADD COLUMN notes TEXT;

DELETE FROM stock_movements;
DELETE FROM products;
DELETE FROM categories;

INSERT INTO categories (name, description) VALUES
${categories.map(category => `('${escapeSql(category)}', 'Categoria importada da planilha do Instituto')`).join(",\n")};

INSERT INTO products (
    name, brand, entity_purpose, unit_of_measure, category_id,
    cost_price, sale_price, quantity, minimum_stock,
    monthly_required_quantity, count_pending, status, notes
) VALUES
${values.join(",\n")};

ALTER TABLE products ADD CONSTRAINT products_status_check
    CHECK (status IN ('NORMAL','SALDO_NEGATIVO','PENDENTE_CONTAGEM','NECESSIDADE_REPOSICAO'));
CREATE INDEX idx_products_count_pending ON products(count_pending);
`;

await fs.mkdir(new URL(".", `file:///${outputPath.replaceAll("\\", "/")}`), { recursive: true });
await fs.writeFile(outputPath, sql, "utf8");
console.log(`Migration gerada: ${rows.length} itens, ${categories.length} categorias.`);
