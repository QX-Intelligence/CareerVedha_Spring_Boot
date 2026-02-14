package com.quinzex.service;

import com.quinzex.entity.Permission;
import com.quinzex.entity.Roles;
import com.quinzex.repository.IPermissionsRepo;
import com.quinzex.repository.RolesRepo;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;
@Service
public class RoleAndPermissionNames implements IRoleAndPermissionNames{

    private final RolesRepo rolesRepo;
    private final IPermissionsRepo permissionsRepo;

    public RoleAndPermissionNames(RolesRepo rolesRepo, IPermissionsRepo permissionsRepo) {
        this.rolesRepo = rolesRepo;
        this.permissionsRepo = permissionsRepo;

    }

    @Override
    public List<String> getPermissionNames() {
        return permissionsRepo.findAll().stream().map(Permission::getName).collect(Collectors.toList());

    }

    @Override
    public List<String> getRoleNames() {
       return rolesRepo.findAll().stream().map(Roles::getRoleName).collect(Collectors.toList());
    }
}
