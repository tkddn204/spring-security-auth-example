package com.rightpair.domain.users.repository;

import com.rightpair.domain.users.entity.User;
import com.rightpair.domain.users.entity.UserConfirm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserConfirmRepository extends JpaRepository<UserConfirm, User> {
    Optional<UserConfirm> findByUser(User user);
}
