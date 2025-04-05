package org.aquariux.crypto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AggregatedPriceResponse {
    private String cryptoPair;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private LocalDateTime lastUpdated;
}
