package org.aquariux.crypto.repository;

import java.util.List;
import org.aquariux.crypto.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByUserIdOrderByTimestampDesc(Long userId);
}
