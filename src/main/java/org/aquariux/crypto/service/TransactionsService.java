package org.aquariux.crypto.service;

import org.aquariux.crypto.dto.TransactionsRequest;
import org.aquariux.crypto.dto.TransactionsResponse;

public interface TransactionsService {
    TransactionsResponse executeTrade(TransactionsRequest transactionsRequest);
}
