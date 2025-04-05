package org.aquariux.crypto.service;

import org.aquariux.crypto.dto.AggregatedPriceResponse;

public interface PriceAggregationService {
    void fetchPrices();

    AggregatedPriceResponse getLatestPrice(String cryptoPair);
}
