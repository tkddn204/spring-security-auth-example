package com.rightpair.domain.users.repository;

import com.rightpair.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u " +
            "INNER JOIN FETCH u.userRoles ur " +
            "INNER JOIN FETCH ur.role r " +
            "WHERE u.email = :email")
    Optional<User> findUsersByEmail(String email);

    boolean existsByEmail(String email);
}
