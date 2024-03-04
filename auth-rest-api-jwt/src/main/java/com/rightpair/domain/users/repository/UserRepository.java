package com.rightpair.domain.users.repository;

import com.rightpair.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    @Query("SELECT u FROM User as u " +
            "INNER JOIN UserConfirm as uc on u = uc.user " +
            "WHERE uc.code = :code")
    Optional<User> findUserByUserConfirmCode(String code);

    boolean existsByEmail(String email);
}
