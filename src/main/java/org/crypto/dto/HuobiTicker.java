package org.crypto.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class HuobiTicker {
    private String symbol;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal amount;
    private BigDecimal vol;
    private int count;
    private BigDecimal bid;
    private BigDecimal bidSize;
    private BigDecimal ask;
    private BigDecimal askSize;
}
