package org.aquariux.crypto.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.aquariux.crypto.dto.HuobiResponse;
import org.aquariux.crypto.entity.AggregatedPrice;
import org.aquariux.crypto.repository.AggregatedPriceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class PriceAggregationServiceImpl implements PriceAggregationService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final AggregatedPriceRepository repository;

    public PriceAggregationServiceImpl(AggregatedPriceRepository repository) {
        this.repository = repository;
    }

    @Override
    @Scheduled(fixedRate = 10000)
    public void fetchPrices() {
        String huobiUrl = "https://api.huobi.pro/market/tickers";
        HuobiResponse huobiResponse = restTemplate.getForObject(huobiUrl, HuobiResponse.class);
        huobiResponse.getData().stream()
                .filter(cryptoTicker ->
                        "ethusdt".equals(cryptoTicker.getSymbol()) || "btcusdt".equals(cryptoTicker.getSymbol()))
                .forEach(cryptoTicker ->
                        saveBestPrice(cryptoTicker.getSymbol(), cryptoTicker.getBid(), cryptoTicker.getAsk()));
    }

    private void saveBestPrice(String cryptoPair, BigDecimal bidPrice, BigDecimal askPrice) {
        AggregatedPrice price = AggregatedPrice.builder()
                .cryptoPair(cryptoPair)
                .bidPrice(bidPrice)
                .askPrice(askPrice)
                .lastUpdated(LocalDateTime.now())
                .build();
        repository.save(price);
        log.info("Saved best price for " + cryptoPair + ": Bid = " + bidPrice + ", Ask = " + askPrice);
    }
}
