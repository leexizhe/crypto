package org.aquariux.crypto.service;

import org.aquariux.crypto.dto.WalletResponse;

public interface WalletService {
    WalletResponse getWallet(Long userId);
}
