package org.aquariux.crypto.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class TransactionsRequest {
    private String cryptoPair;
    private BigDecimal quantity;
    private String tradeType;
    private Long userId;
}
