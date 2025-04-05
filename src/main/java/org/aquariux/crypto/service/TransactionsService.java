package org.aquariux.crypto.service;

import java.util.List;
import org.aquariux.crypto.dto.TransactionsRequest;
import org.aquariux.crypto.dto.TransactionsResponse;

public interface TransactionsService {

    List<TransactionsResponse> getUserTradeHistory(Long userId);

    TransactionsResponse executeTrade(TransactionsRequest transactionsRequest);
}
