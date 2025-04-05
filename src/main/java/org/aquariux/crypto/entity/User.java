package org.aquariux.crypto.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private BigDecimal walletBalance;
}
