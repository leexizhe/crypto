package org.aquariux.crypto.utils;

import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "crypto")
public class CryptoConfig {
    private Map<String, String> exchangeUrls;
    private Set<String> trackedPairs;
}
