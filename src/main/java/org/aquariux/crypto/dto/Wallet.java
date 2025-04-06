package org.aquariux.crypto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    private String cryptoPair;
    private BigDecimal quantity;
    private LocalDateTime timestamp;
}
