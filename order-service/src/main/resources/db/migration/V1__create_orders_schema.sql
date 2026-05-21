CREATE SCHEMA IF NOT EXISTS orders;

CREATE TABLE IF NOT EXISTS orders.orders (
    id UUID PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL,
    date TIMESTAMP NOT NULL,
    total NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders.order_items (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    total NUMERIC(10, 2) NOT NULL,
    position INTEGER NOT NULL,
    order_id UUID NOT NULL,
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
        REFERENCES orders.orders (id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_orders_account_date
    ON orders.orders (account_id, date);

CREATE INDEX IF NOT EXISTS idx_order_items_order
    ON orders.order_items (order_id);
