package org.crypto.repository;

import org.crypto.entity.AggregatedPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<AggregatedPrice, Long> {
    AggregatedPrice findTopByCryptoPairOrderByTimestampDesc(String cryptoPair);
}
