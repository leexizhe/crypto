package org.aquariux.crypto.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BinanceResponse {
    private String symbol;
    private BigDecimal bidPrice;
    private BigDecimal bidQty;
    private BigDecimal askPrice;
    private BigDecimal askQty;
}
