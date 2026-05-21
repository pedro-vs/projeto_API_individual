CREATE SCHEMA IF NOT EXISTS accounts;

CREATE TABLE IF NOT EXISTS accounts.accounts (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    CONSTRAINT uk_accounts_email UNIQUE (email)
);
