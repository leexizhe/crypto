package org.crypto.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class TransactionsRequest {
    @NotNull
    private String cryptoPair;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private TradeType tradeType;

    @NotNull
    private Long userId;
}
