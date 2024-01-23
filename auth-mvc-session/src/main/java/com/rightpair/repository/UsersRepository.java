package com.rightpair.repository;

import com.rightpair.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUsersByEmail(String email);
}
