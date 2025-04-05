package org.aquariux.crypto.dto;

import java.util.List;
import lombok.Data;

@Data
public class HuobiResponse {
    private List<CryptoTicker> data;
    private String status;
    private long timestamp;
}
