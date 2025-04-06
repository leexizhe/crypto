package org.crypto.service;

import java.util.List;
import org.crypto.dto.TransactionsRequest;
import org.crypto.dto.TransactionsResponse;

public interface TransactionsService {

    List<TransactionsResponse> getUserTradeHistory(Long userId);

    TransactionsResponse executeTrade(TransactionsRequest transactionsRequest);
}
