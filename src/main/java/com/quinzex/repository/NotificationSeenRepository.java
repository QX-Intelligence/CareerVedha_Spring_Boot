package com.quinzex.repository;

import com.quinzex.entity.NotificationSeen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface NotificationSeenRepository extends JpaRepository<NotificationSeen,Long> {

    @Query("""
   SELECT ns.notificationId
   FROM NotificationSeen ns
   WHERE ns.userId = :userId
   AND ns.notificationId IN :notificationIds
""")
    Set<Long> findAlreadySeenIds(Long userId, Set<Long> notificationIds);

}
