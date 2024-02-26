package com.rightpair.domain.users.repository;

import com.rightpair.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUsersByEmail(String email);
    boolean existsByEmail(String email);
}
