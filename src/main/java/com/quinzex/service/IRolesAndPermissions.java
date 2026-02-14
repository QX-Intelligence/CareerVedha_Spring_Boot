package com.quinzex.service;

import com.quinzex.dto.CreatePermissionRequest;
import com.quinzex.dto.CreateRoleRequest;
import com.quinzex.dto.RolePermissionRequest;
import com.quinzex.entity.Roles;

import java.util.List;


public interface IRolesAndPermissions {

    public String createPermission( CreatePermissionRequest permissionRequest);
    public String deletePermission(CreatePermissionRequest permissionRequest);
    public String createRole(String roleName);
    public String inactiveRole(String roleName);
    public String addPermissionToRole(RolePermissionRequest request);
    public String removePermissionFromRole(RolePermissionRequest request);
    public List<String> getAllActiveRoles();
    public List<String> getAllInactiveRoles();
    public String activeRole(String roleName);

}
