package com.quinzex.repository;

import com.quinzex.entity.Permission;
import com.quinzex.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolesRepo extends JpaRepository<Roles,Long> {
    Optional<Roles> findByRoleName(String roleName);
        List<Roles> findAllByPermissions(Permission permission);
    List<Roles> findByRoleStatusTrue();
    List<Roles> findByRoleStatusFalse();


}
