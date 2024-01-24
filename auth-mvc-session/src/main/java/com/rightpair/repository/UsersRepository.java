package com.rightpair.repository;

import com.rightpair.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    @Query("SELECT u FROM Users u " +
            "INNER JOIN FETCH u.userRoles ur " +
            "INNER JOIN FETCH ur.roles r " +
            "WHERE u.email = :email")
    Optional<Users> findUsersByEmail(String email);

    boolean existsByEmail(String email);
}
