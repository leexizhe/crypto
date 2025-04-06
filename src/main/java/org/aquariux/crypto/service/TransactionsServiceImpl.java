package org.aquariux.crypto.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aquariux.crypto.dto.TransactionsRequest;
import org.aquariux.crypto.dto.TransactionsResponse;
import org.aquariux.crypto.entity.AggregatedPrice;
import org.aquariux.crypto.entity.Transactions;
import org.aquariux.crypto.exception.ApplicationException;
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
    public List<TransactionsResponse> getUserTradeHistory(Long userId) {

        List<Transactions> transactionsList = transactionsRepository.findByUserIdOrderByTimestampDesc(userId);
        log.info("Transactions list size: {}", transactionsList.size());
        return transactionsList.stream()
                .map(transactions -> new TransactionsResponse(
                        transactions.getCryptoPair(),
                        transactions.getCost(),
                        transactions.getQuantity(),
                        transactions.getExecutionPrice(),
                        transactions.getTradeType(),
                        transactions.getTimestamp()))
                .toList();
    }

    @Override
    public TransactionsResponse executeTrade(TransactionsRequest request) {
        AggregatedPrice latestPrice =
                priceRepository.findTopByCryptoPairOrderByLastUpdatedDesc(request.getCryptoPair());

        if (latestPrice == null) {
            throw new ApplicationException("No price data available for " + request.getCryptoPair());
        }

        BigDecimal executionPrice =
                request.getTradeType().equals("BUY") ? latestPrice.getAskPrice() : latestPrice.getBidPrice();
        BigDecimal cost = request.getQuantity().multiply(executionPrice);

        Transactions transactions = new Transactions(
                request.getCryptoPair(),
                cost,
                request.getQuantity(),
                executionPrice,
                request.getTradeType(),
                request.getUserId(),
                LocalDateTime.now());
        transactionsRepository.save(transactions);

        return new TransactionsResponse(
                transactions.getCryptoPair(),
                transactions.getCost(),
                transactions.getQuantity(),
                transactions.getExecutionPrice(),
                transactions.getTradeType(),
                transactions.getTimestamp());
    }
}
