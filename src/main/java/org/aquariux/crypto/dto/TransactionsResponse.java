package org.aquariux.crypto.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class TransactionsResponse {
    private String cryptoPair;
    private BigDecimal amount;
    private BigDecimal executionPrice;
    private String tradeType;
    private String message;
}
