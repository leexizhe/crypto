package org.aquariux.crypto.service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aquariux.crypto.dto.TransactionsRequest;
import org.aquariux.crypto.dto.TransactionsResponse;
import org.aquariux.crypto.entity.AggregatedPrice;
import org.aquariux.crypto.entity.Transactions;
import org.aquariux.crypto.entity.User;
import org.aquariux.crypto.entity.UserWallet;
import org.aquariux.crypto.exception.ApplicationException;
import org.aquariux.crypto.repository.PriceRepository;
import org.aquariux.crypto.repository.TransactionsRepository;
import org.aquariux.crypto.repository.UserRepository;
import org.aquariux.crypto.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TransactionsServiceImpl implements TransactionsService {
    private final PriceRepository priceRepository;
    private final TransactionsRepository transactionsRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

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
        AggregatedPrice latestPrice = priceRepository.findTopByCryptoPairOrderByTimestampDesc(request.getCryptoPair());

        if (latestPrice == null) {
            throw new ApplicationException("No price data available for " + request.getCryptoPair());
        }

        BigDecimal executionPrice =
                request.getTradeType().equals("BUY") ? latestPrice.getAskPrice() : latestPrice.getBidPrice();
        BigDecimal cost = request.getQuantity().multiply(executionPrice);

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new ApplicationException("User not found"));

        if (request.getTradeType().equals("BUY")) {
            if (user.getWalletBalance().compareTo(cost) < 0) {
                throw new ApplicationException("Insufficient wallet balance");
            }
            user.setWalletBalance(user.getWalletBalance().subtract(cost));

            Optional<UserWallet> existingWallet =
                    walletRepository.findByUserIdAndCryptoPair(request.getUserId(), request.getCryptoPair());

            if (existingWallet.isPresent()) {
                UserWallet userWallet = existingWallet.get();
                userWallet.setQuantity(userWallet.getQuantity().add(request.getQuantity()));
                walletRepository.save(userWallet);
            } else {
                UserWallet newWallet = new UserWallet();
                newWallet.setUserId(request.getUserId());
                newWallet.setCryptoPair(request.getCryptoPair());
                newWallet.setQuantity(request.getQuantity());
                walletRepository.save(newWallet);
            }
        } else {
            UserWallet userWallet = walletRepository
                    .findByUserIdAndCryptoPair(request.getUserId(), request.getCryptoPair())
                    .orElseThrow(() -> new ApplicationException("No wallet found for " + request.getCryptoPair()));

            if (userWallet.getQuantity().compareTo(request.getQuantity()) < 0) {
                throw new ApplicationException("Insufficient wallet quantity to sell");
            }
            userWallet.setQuantity(userWallet.getQuantity().subtract(request.getQuantity()));
            user.setWalletBalance(user.getWalletBalance().add(cost));
            walletRepository.save(userWallet);

            if (userWallet.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                walletRepository.delete(userWallet);
            } else {
                walletRepository.save(userWallet);
            }
        }

        userRepository.save(user);

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
