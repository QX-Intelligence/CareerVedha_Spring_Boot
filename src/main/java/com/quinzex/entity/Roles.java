package com.quinzex.entity;

import jakarta.persistence.*;

import lombok.*;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true,name = "role_name")
    private String roleName;

    private int roleVersion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();
@Column(name = "role_status",nullable = false)
    private Boolean roleStatus=true;
}
