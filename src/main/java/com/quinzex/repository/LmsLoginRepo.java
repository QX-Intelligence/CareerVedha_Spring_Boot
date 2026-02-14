package com.quinzex.repository;

import com.quinzex.entity.LmsLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LmsLoginRepo extends JpaRepository<LmsLogin,String> {
    boolean existsByEmail(String email);
    Optional<LmsLogin> findByEmail(String email);
    boolean existsByRole_RoleName(String roleName);
    Page<LmsLogin> findByIsAuthorized(Boolean isAuthorized, Pageable pageable);

    @Query("""
        SELECT u FROM LmsLogin u
        WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.lastName)  LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.email)     LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<LmsLogin> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT l FROM LmsLogin l
    JOIN FETCH l.role r
    JOIN FETCH r.permissions
    WHERE l.email = :email
""")
    Optional<LmsLogin> findByEmailWithRoleAndPermissions(String email);

}
