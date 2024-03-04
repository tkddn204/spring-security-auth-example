package com.rightpair.domain.users.repository;

import com.rightpair.domain.users.entity.UserRole;
import com.rightpair.domain.users.entity.types.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UsersRoleId> {
    Set<UserRole> findAllByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM UserRole as ur " +
            "WHERE ur.user.id = :userId and ur.role.roleType = :roleType")
    void deleteAllByUserIdAndRoleType(Long userId, RoleType roleType);
}