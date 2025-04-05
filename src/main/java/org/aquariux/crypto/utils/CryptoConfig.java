package org.aquariux.crypto.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class CryptoConfig {
    @Value("${crypto.url.binance}")
    private String binanceUrl;

    @Value("${crypto.url.huobi}")
    private String huobiUrl;
}
