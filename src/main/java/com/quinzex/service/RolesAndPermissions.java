package com.quinzex.service;

import com.quinzex.dto.CreatePermissionRequest;
import com.quinzex.dto.RolePermissionRequest;
import com.quinzex.entity.Permission;
import com.quinzex.entity.Roles;
import com.quinzex.repository.IPermissionsRepo;
import com.quinzex.repository.RolesRepo;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesAndPermissions implements IRolesAndPermissions {

    private final RolesRepo rolesRepo;
    private final IPermissionsRepo permissionsRepo;

    public RolesAndPermissions(RolesRepo rolesRepo , IPermissionsRepo iPermissionsRepo){
        this.rolesRepo =rolesRepo;
        this.permissionsRepo=iPermissionsRepo;
    }
    @Override
    public String createPermission(CreatePermissionRequest permissionRequest) {
        String name = permissionRequest.getPermissionName().toUpperCase();
        if(permissionsRepo.findByName(name).isPresent()){
            throw new RuntimeException("Permission already exists: " +name);
        }
        Permission permission = new Permission();
        permission.setName(name);

        permissionsRepo.save(permission);
        return "Nice job...! A new Permission is Added";
    }

    @Override
    @Transactional
    public String deletePermission(CreatePermissionRequest request) {

        String name = request.getPermissionName().toUpperCase();

        Permission permission = permissionsRepo.findByName(name)
                .orElseThrow(() ->
                        new RuntimeException("Permission not found: " + name)
                );
        List<Roles> roles = rolesRepo.findAllByPermissions(permission);
        for (Roles role : roles){
            role.getPermissions().remove(permission);
            role.setRoleVersion(role.getRoleVersion()+1);
        }

        rolesRepo.saveAll(roles);
        permissionsRepo.delete(permission);
        return "Permission deleted successfully";
    }


    @Override
    @PreAuthorize("hasAuthority('LOGIN')")
    public String createRole(String roleName) {
        String name = roleName.toUpperCase();
        if (rolesRepo.findByRoleName(name).isPresent()){
            throw new RuntimeException("Role already exists: " + name);
        }
        Roles roles = new Roles();
        roles.setRoleName(name);
        rolesRepo.save(roles);
        return "Nice job...! A  new Role is Created";
    }

    @Override
    public String inactiveRole(String roleName) {
        String name = roleName.toUpperCase();
        if ("SUPER_ADMIN".equals(name)) {
            throw new IllegalStateException("You can't inactivate SUPER_ADMIN role");
        }

        Roles roles = rolesRepo.findByRoleName(name).orElseThrow(()->new RuntimeException("Role not Found" +name));
        if(!Boolean.TRUE.equals(roles.getRoleStatus())){
            throw new RuntimeException("Role status already in inactive");
        }
        roles.setRoleStatus(false);
        roles.setRoleVersion(roles.getRoleVersion()+1);
        rolesRepo.save(roles);
        return "Role successfully inactivated";
    }

    @Override
    public String activeRole(String roleName) {
        String name = roleName.toUpperCase();
        Roles  roles =rolesRepo.findByRoleName(name).orElseThrow(()->new RuntimeException("Role not found" +name));
        if(Boolean.TRUE.equals(roles.getRoleStatus())){
            throw new RuntimeException("Role status already in active");
        }
        roles.setRoleStatus(true);
        roles.setRoleVersion(roles.getRoleVersion()+1);
        rolesRepo.save(roles);
        return "Role successfully activated";
    }
    @Transactional
    @Override
    public String addPermissionToRole(RolePermissionRequest request) {

        String roleName = request.getRoleName().toUpperCase();
        String permissionName = request.getPermissionName().toUpperCase();
        Roles role = rolesRepo.findByRoleName(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role not found: " + roleName)
                );
        Permission permission = permissionsRepo.findByName(permissionName)
                .orElseThrow(() ->
                        new RuntimeException("Permission not found: " + permissionName)
                );

        if (role.getPermissions().contains(permission)) {
            throw new RuntimeException(
                    "Permission " + permissionName + " already assigned to role " + roleName
            );
        }
        role.getPermissions().add(permission);
        role.setRoleVersion(role.getRoleVersion()+1);
        rolesRepo.save(role);
        return "Permission added to role successfully";
    }

    @Transactional
    @Override
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public String removePermissionFromRole(RolePermissionRequest request) {
        String currentUserRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElseThrow(()->new AccessDeniedException("current user role not found")).replace("ROLE_", "");
        String roleName = request.getRoleName().toUpperCase();
        String permissionName = request.getPermissionName().toUpperCase();
        if ("ADMIN".equals(currentUserRole) && "SUPER_ADMIN".equals(roleName)) {
            throw new AccessDeniedException("ADMIN cannot modify SUPER_ADMIN permissions");
        }
        Roles role = rolesRepo.findByRoleName(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role not found: " + roleName)
                );

        Permission permission = permissionsRepo.findByName(permissionName)
                .orElseThrow(() ->
                        new RuntimeException("Permission not found: " + permissionName)
                );

        if (!role.getPermissions().contains(permission)) {
            throw new RuntimeException("Permission not assigned to role");
        }

        role.getPermissions().remove(permission);
        role.setRoleVersion(role.getRoleVersion()+1);
        rolesRepo.save(role);

        return "Permission removed from role successfully";

    }

    @Transactional
    @Override
    public List<String> getAllActiveRoles(){
        return rolesRepo.findByRoleStatusTrue().stream().map(Roles::getRoleName).toList();
    }
    @Transactional
    @Override
    public List<String> getAllInactiveRoles(){
        return rolesRepo.findByRoleStatusFalse().stream().map(Roles::getRoleName).toList();
    }
}