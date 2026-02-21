package com.quinzex.repository;

import com.quinzex.entity.PostNotification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostNotificationRepo extends JpaRepository<PostNotification,Long> {


    List<PostNotification> findByReceiverRole(String receiverRole, Sort sort);

    @Query("""
    SELECT p FROM PostNotification p
    WHERE (p.createdAt < :createdAt)
       OR (p.createdAt = :createdAt AND p.notificationId < :id)
    ORDER BY p.createdAt DESC, p.notificationId DESC
""")
    List<PostNotification> findAllByCursor(
            @Param("createdAt") LocalDateTime createdAt,
            @Param("id") Long id,
            Pageable pageable
    );

    @Query("""
    SELECT p FROM PostNotification p
    WHERE p.receiverRole <> 'SUPER_ADMIN'
      AND (
           p.createdAt < :createdAt
        OR (p.createdAt = :createdAt AND p.notificationId < :id)
      )
    ORDER BY p.createdAt DESC, p.notificationId DESC
""")
    List<PostNotification> findAllExcludingSuperAdminByCursor(
            @Param("createdAt") LocalDateTime createdAt,
            @Param("id") Long id,
            Pageable pageable
    );

    @Query("""
    SELECT p FROM PostNotification p
    WHERE p.receiverRole = :role
      AND (
           p.createdAt < :createdAt
        OR (p.createdAt = :createdAt AND p.notificationId < :id)
      )
    ORDER BY p.createdAt DESC, p.notificationId DESC
""")
    List<PostNotification> findByRoleWithCursor(
            @Param("role") String role,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("id") Long id,
            Pageable pageable
    );

    @Modifying
    @Query("""
    UPDATE PostNotification p
    SET p.seen = true
    WHERE p.notificationId = :id
""")
    int markAsSeen(@Param("id") Long id);


}
