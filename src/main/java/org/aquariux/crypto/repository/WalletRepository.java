package org.aquariux.crypto.repository;

import java.util.List;
import java.util.Optional;
import org.aquariux.crypto.entity.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<UserWallet, Long> {
    List<UserWallet> findByUserId(Long userId);

    Optional<UserWallet> findByUserIdAndCryptoPair(Long userId, String cryptoPair);
}
