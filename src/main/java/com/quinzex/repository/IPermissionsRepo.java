package com.quinzex.repository;

import com.quinzex.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPermissionsRepo extends JpaRepository<Permission, Long> {
          Optional<Permission> findByName(String name);
}
