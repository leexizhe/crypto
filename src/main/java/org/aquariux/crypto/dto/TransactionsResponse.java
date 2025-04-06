package org.aquariux.crypto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsResponse {
    private String cryptoPair;
    private BigDecimal cost;
    private BigDecimal quantity;
    private BigDecimal executionPrice;
    private TradeType tradeType;
    private LocalDateTime timestamp;
}
