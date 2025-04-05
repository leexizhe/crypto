package org.aquariux.crypto.service;

import static org.aquariux.crypto.utils.CryptoConstant.BTCUSDT;
import static org.aquariux.crypto.utils.CryptoConstant.ETHUSDT;

import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aquariux.crypto.dto.AggregatedPriceResponse;
import org.aquariux.crypto.dto.BinanceResponse;
import org.aquariux.crypto.dto.HuobiResponse;
import org.aquariux.crypto.entity.AggregatedPrice;
import org.aquariux.crypto.repository.PriceRepository;
import org.aquariux.crypto.utils.CryptoConfig;
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
    @Scheduled(fixedRate = 10000)
    public void fetchPrices() {
        List<AggregatedPrice> binancePrices = callBinance();
        List<AggregatedPrice> huobPrices = callHuobi();

        List<AggregatedPrice> aggregatedPrices = comparePrices(binancePrices, huobPrices);
        aggregatedPrices.stream().forEach(this::saveBestPrice);
    }

    @Override
    public AggregatedPriceResponse getLatestPrice(String cryptoPair) {
        AggregatedPrice aggregatedPrice = repository.findTopByCryptoPairOrderByLastUpdatedDesc(cryptoPair);
        return new AggregatedPriceResponse(
                aggregatedPrice.getCryptoPair(),
                aggregatedPrice.getBidPrice(),
                aggregatedPrice.getAskPrice(),
                aggregatedPrice.getLastUpdated());
    }

    private List<AggregatedPrice> callBinance() {
        try {
            BinanceResponse[] response =
                    restTemplate.getForObject(cryptoConfig.getBinanceUrl(), BinanceResponse[].class);

            if (response == null) {
                log.error("Received null response or empty data from Binance API");
                return Collections.emptyList();
            }

            return Arrays.stream(response)
                    .filter(ticker -> BTCUSDT.equalsIgnoreCase(ticker.getSymbol())
                            || ETHUSDT.equalsIgnoreCase(ticker.getSymbol()))
                    .map(ticker -> new AggregatedPrice(
                            ticker.getSymbol().toUpperCase(), ticker.getBidPrice(), ticker.getAskPrice()))
                    .toList();

        } catch (Exception e) {
            log.error("Error fetching Binance data: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private List<AggregatedPrice> callHuobi() {
        try {
            HuobiResponse response = restTemplate.getForObject(cryptoConfig.getHuobiUrl(), HuobiResponse.class);

            if (response == null || response.getData() == null) {
                log.error("Received null response or empty data from Huobi API");
                return Collections.emptyList();
            }

            return response.getData().stream()
                    .filter(ticker -> BTCUSDT.equalsIgnoreCase(ticker.getSymbol())
                            || ETHUSDT.equalsIgnoreCase(ticker.getSymbol()))
                    .map(ticker ->
                            new AggregatedPrice(ticker.getSymbol().toUpperCase(), ticker.getBid(), ticker.getAsk()))
                    .toList();

        } catch (Exception e) {
            log.error("Error fetching Huobi data: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public static List<AggregatedPrice> comparePrices(List<AggregatedPrice> list1, List<AggregatedPrice> list2) {
        Map<String, AggregatedPrice> priceMap = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        list1.forEach(price -> priceMap.put(
                price.getCryptoPair(),
                new AggregatedPrice(price.getCryptoPair(), price.getBidPrice(), price.getAskPrice())));

        list2.forEach(price -> priceMap.merge(
                price.getCryptoPair(),
                new AggregatedPrice(price.getCryptoPair(), price.getBidPrice(), price.getAskPrice()),
                (existing, newPrice) -> new AggregatedPrice(
                        price.getCryptoPair(),
                        existing.getBidPrice().max(newPrice.getBidPrice()),
                        existing.getAskPrice().min(newPrice.getAskPrice()),
                        now)));

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
