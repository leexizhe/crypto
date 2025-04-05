package org.aquariux.crypto.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aquariux.crypto.dto.TransactionsRequest;
import org.aquariux.crypto.dto.TransactionsResponse;
import org.aquariux.crypto.entity.AggregatedPrice;
import org.aquariux.crypto.entity.Transactions;
import org.aquariux.crypto.repository.PriceRepository;
import org.aquariux.crypto.repository.TransactionsRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {
    private final PriceRepository priceRepository;
    private final TransactionsRepository transactionsRepository;

    @Override
    public TransactionsResponse executeTrade(TransactionsRequest request) {
        TransactionsResponse response = new TransactionsResponse();
        AggregatedPrice latestPrice =
                priceRepository.findTopByCryptoPairOrderByLastUpdatedDesc(request.getCryptoPair());

        if (latestPrice == null) {
            response.setMessage("No price data available for " + request.getCryptoPair());
            return response;
        }

        BigDecimal executionPrice =
                request.getTradeType().equals("BUY") ? latestPrice.getAskPrice() : latestPrice.getBidPrice();

        Transactions transactions = new Transactions(
                request.getCryptoPair(),
                request.getAmount(),
                executionPrice,
                request.getTradeType(),
                request.getUserId(),
                LocalDateTime.now());
        transactionsRepository.save(transactions);

        response.setCryptoPair(request.getCryptoPair());
        response.setAmount(executionPrice);
        response.setExecutionPrice(executionPrice);
        response.setTradeType(request.getTradeType());
        response.setMessage("SUCCESS");

        return response;
    }
}
