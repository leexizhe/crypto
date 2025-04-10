CREATE TABLE users
(
    user_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    username       VARCHAR(255)   NOT NULL,
    wallet_balance DECIMAL(18, 8) NOT NULL
);

CREATE TABLE aggregated_prices
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    crypto_pair VARCHAR(10)    NOT NULL,
    bid_price   DECIMAL(18, 8) NOT NULL,
    ask_price   DECIMAL(18, 8) NOT NULL,
    timestamp   TIMESTAMP
);

CREATE TABLE transactions
(
    transactions_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    crypto_pair     VARCHAR(10)    NOT NULL,
    cost            DECIMAL(18, 8) NOT NULL,
    quantity        DECIMAL(18, 8) NOT NULL,
    execution_price DECIMAL(18, 8) NOT NULL,
    trade_type      VARCHAR(4)     NOT NULL,
    user_id         BIGINT         NOT NULL,
    timestamp       TIMESTAMP
);

CREATE TABLE user_wallet
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT         NOT NULL,
    crypto_pair  VARCHAR(20)    NOT NULL,
    quantity     DECIMAL(18, 8) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (user_id, crypto_pair)
);