package com.quinzex.dto;

import lombok.Data;

@Data
public class RolePermissionRequest {
    private String roleName;
    private String permissionName;
}
