package org.crypto.service;

import org.crypto.dto.AggregatedPriceResponse;

public interface PriceAggregationService {
    void fetchPrices();

    AggregatedPriceResponse getLatestPrice(String cryptoPair);
}
