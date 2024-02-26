package com.rightpair.global.common;

import com.rightpair.domain.users.entity.Role;
import com.rightpair.domain.users.entity.types.RoleType;
import com.rightpair.domain.users.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDBLoader implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initRoles();
    }

    private void initRoles() {
        for (RoleType roleType : RoleType.values()) {
            if (!roleRepository.existsByRoleType(roleType)) {
                roleRepository.save(new Role(roleType));
            }
        }
    }
}
