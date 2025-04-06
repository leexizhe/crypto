package org.aquariux.crypto.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aquariux.crypto.dto.Wallet;
import org.aquariux.crypto.dto.WalletResponse;
import org.aquariux.crypto.entity.User;
import org.aquariux.crypto.entity.UserWallet;
import org.aquariux.crypto.exception.ApplicationException;
import org.aquariux.crypto.repository.UserRepository;
import org.aquariux.crypto.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Override
    public WalletResponse getWallet(Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new ApplicationException("User not found"));

        List<UserWallet> holdings = walletRepository.findByUserId(userId);
        List<Wallet> list = holdings.stream()
                .map(userWallet ->
                        new Wallet(userWallet.getCryptoPair(), userWallet.getQuantity(), userWallet.getLastUpdated()))
                .toList();

        return new WalletResponse(String.valueOf(user.getUserId()), user.getWalletBalance(), list);
    }
}
