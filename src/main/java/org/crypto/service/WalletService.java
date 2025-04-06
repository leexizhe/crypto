package org.crypto.service;

import org.crypto.dto.WalletResponse;

public interface WalletService {
    WalletResponse getWallet(Long userId);
}
