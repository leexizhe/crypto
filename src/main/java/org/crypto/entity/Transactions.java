package org.crypto.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.crypto.dto.TradeType;

@Data
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class Transactions {
    @Id
    @Column(name = "transactions_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionsId;

    private String cryptoPair;
    private BigDecimal cost;
    private BigDecimal quantity;
    private BigDecimal executionPrice;

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    private Long userId;
    private LocalDateTime timestamp;

    public Transactions(
            String cryptoPair,
            BigDecimal cost,
            BigDecimal quantity,
            BigDecimal executionPrice,
            TradeType tradeType,
            Long userId,
            LocalDateTime timestamp) {
        this.cryptoPair = cryptoPair;
        this.cost = cost;
        this.quantity = quantity;
        this.executionPrice = executionPrice;
        this.tradeType = tradeType;
        this.userId = userId;
        this.timestamp = timestamp;
    }
}
