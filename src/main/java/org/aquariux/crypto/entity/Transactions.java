package org.aquariux.crypto.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

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
    private BigDecimal amount;
    private BigDecimal executionPrice;
    private String tradeType;
    private Long userId;
    private LocalDateTime timestamp;

    public Transactions(
            String cryptoPair,
            BigDecimal amount,
            BigDecimal executionPrice,
            String tradeType,
            Long userId,
            LocalDateTime timestamp) {
        this.cryptoPair = cryptoPair;
        this.amount = amount;
        this.executionPrice = executionPrice;
        this.tradeType = tradeType;
        this.userId = userId;
        this.timestamp = timestamp;
    }
}
