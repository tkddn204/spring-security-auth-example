package com.rightpair.domain.users.repository;

import com.rightpair.domain.users.entity.Role;
import com.rightpair.domain.users.entity.types.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRolesType(RoleType roleType);
}
