package org.crypto.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crypto.dto.Wallet;
import org.crypto.dto.WalletResponse;
import org.crypto.entity.User;
import org.crypto.entity.UserWallet;
import org.crypto.exception.ApplicationException;
import org.crypto.repository.UserRepository;
import org.crypto.repository.WalletRepository;
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
                        new Wallet(userWallet.getCryptoPair(), userWallet.getQuantity(), userWallet.getTimestamp()))
                .toList();

        return new WalletResponse(String.valueOf(user.getUserId()), user.getWalletBalance(), list);
    }
}
