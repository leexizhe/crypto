package org.crypto.repository;

import java.util.Optional;
import org.crypto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
}
