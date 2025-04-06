package org.crypto.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crypto.dto.AggregatedPriceResponse;
import org.crypto.service.PriceAggregationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prices")
@Slf4j
@RequiredArgsConstructor
public class AggregatedPriceController {
    private final PriceAggregationService service;

    @GetMapping("/{cryptoPair}")
    public AggregatedPriceResponse getLatestPrice(@PathVariable String cryptoPair) {
        return service.getLatestPrice(cryptoPair);
    }
}
