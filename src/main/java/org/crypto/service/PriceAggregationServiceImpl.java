package org.crypto.service;

import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crypto.dto.AggregatedPriceResponse;
import org.crypto.dto.BinanceResponse;
import org.crypto.dto.HuobiResponse;
import org.crypto.entity.AggregatedPrice;
import org.crypto.repository.PriceRepository;
import org.crypto.utils.CryptoConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceAggregationServiceImpl implements PriceAggregationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final PriceRepository repository;
    private final CryptoConfig cryptoConfig;

    @Override
    public AggregatedPriceResponse getLatestPrice(String cryptoPair) {
        AggregatedPrice aggregatedPrice = repository.findTopByCryptoPairOrderByTimestampDesc(cryptoPair.toUpperCase());
        return new AggregatedPriceResponse(
                aggregatedPrice.getCryptoPair(),
                aggregatedPrice.getBidPrice(),
                aggregatedPrice.getAskPrice(),
                aggregatedPrice.getTimestamp());
    }

    @Override
    @Scheduled(fixedRate = 10000)
    public void fetchPrices() {
        List<List<AggregatedPrice>> priceSources = cryptoConfig.getExchangeUrls().entrySet().stream()
                .map(entry -> fetchPricesFromApi(entry.getKey(), entry.getValue()))
                .toList();

        List<AggregatedPrice> aggregatedPrices = comparePrices(priceSources);
        aggregatedPrices.forEach(this::saveBestPrice);
    }

    private List<AggregatedPrice> fetchPricesFromApi(String exchangeName, String url) {
        return switch (exchangeName.toLowerCase()) {
            case "binance" -> callBinance(url);
            case "huobi" -> callHuobi(url);
            default -> {
                log.warn("Unhandled exchange: {}", exchangeName);
                yield Collections.emptyList();
            }
        };
    }

    private List<AggregatedPrice> callBinance(String url) {
        try {
            BinanceResponse[] response = restTemplate.getForObject(url, BinanceResponse[].class);

            if (response == null) {
                log.error("Received null response or empty data from Binance API");
                return Collections.emptyList();
            }

            return Arrays.stream(response)
                    .filter(ticker -> cryptoConfig
                            .getTrackedPairs()
                            .contains(ticker.getSymbol().toUpperCase()))
                    .map(ticker -> new AggregatedPrice(ticker.getSymbol(), ticker.getBidPrice(), ticker.getAskPrice()))
                    .toList();
        } catch (Exception e) {
            log.error("Error fetching Binance data: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private List<AggregatedPrice> callHuobi(String url) {
        try {
            HuobiResponse response = restTemplate.getForObject(url, HuobiResponse.class);

            if (response == null || response.getData() == null) {
                log.error("Received null response or empty data from Huobi API");
                return Collections.emptyList();
            }

            return response.getData().stream()
                    .filter(ticker -> cryptoConfig
                            .getTrackedPairs()
                            .contains(ticker.getSymbol().toUpperCase()))
                    .map(ticker ->
                            new AggregatedPrice(ticker.getSymbol().toUpperCase(), ticker.getBid(), ticker.getAsk()))
                    .toList();
        } catch (Exception e) {
            log.error("Error fetching Huobi data: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private List<AggregatedPrice> comparePrices(List<List<AggregatedPrice>> priceSources) {
        Map<String, AggregatedPrice> priceMap = new HashMap<>();

        priceSources.forEach(source -> source.forEach(price -> priceMap.merge(
                price.getCryptoPair(),
                new AggregatedPrice(price.getCryptoPair(), price.getBidPrice(), price.getAskPrice()),
                (existing, newPrice) -> new AggregatedPrice(
                        price.getCryptoPair(),
                        existing.getBidPrice().max(newPrice.getBidPrice()),
                        existing.getAskPrice().min(newPrice.getAskPrice())))));

        return new ArrayList<>(priceMap.values());
    }

    private void saveBestPrice(AggregatedPrice price) {
        repository.save(price);
        log.info(
                "Saved best price for {} Bid = {} Ask = {} ",
                price.getCryptoPair(),
                price.getBidPrice(),
                price.getAskPrice());
    }
}
