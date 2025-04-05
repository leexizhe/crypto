package org.aquariux.crypto.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "aggregated_prices")
public class AggregatedPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cryptoPair;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private LocalDateTime lastUpdated;

    public AggregatedPrice(String cryptoPair, BigDecimal bidPrice, BigDecimal askPrice) {
        this.cryptoPair = cryptoPair;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    public AggregatedPrice(String cryptoPair, BigDecimal bidPrice, BigDecimal askPrice, LocalDateTime lastUpdated) {
        this.cryptoPair = cryptoPair;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.lastUpdated = lastUpdated;
    }
}
