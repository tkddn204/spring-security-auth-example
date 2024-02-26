package com.rightpair.domain.users.repository;

import com.rightpair.domain.users.entity.User;
import com.rightpair.infra.security.AuthUserDetailsRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserDetailsRepository extends Repository<User, Long>, AuthUserDetailsRepository<User> {
    @Query("SELECT u FROM User u " +
            "INNER JOIN FETCH u.userRoles ur " +
            "INNER JOIN FETCH ur.role r " +
            "WHERE u.email = :email")
    Optional<User> findUsersByEmail(String email);
}
