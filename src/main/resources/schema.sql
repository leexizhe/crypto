CREATE TABLE users
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    username       VARCHAR(255)   NOT NULL,
    wallet_balance DECIMAL(18, 8) NOT NULL
);

CREATE TABLE aggregated_prices
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    crypto_pair  VARCHAR(10)    NOT NULL,
    bid_price    DECIMAL(18, 8) NOT NULL,
    ask_price    DECIMAL(18, 8) NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);