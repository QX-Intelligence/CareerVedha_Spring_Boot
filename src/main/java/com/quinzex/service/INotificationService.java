package com.quinzex.service;

import com.quinzex.entity.RoleNotification;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface INotificationService {

    public RoleNotification sendNotification(String mail,String role,String message);
    public List<RoleNotification> getAllNotifications(LocalDateTime cursorTime,Long cursorId,int limit);

    public List<RoleNotification> getNotificationsByRole(Authentication authentication, LocalDateTime cursorTime, Long cursorId, int limit);
    public List<RoleNotification> getUnseenNotificationsByRole(Authentication authentication,LocalDateTime cursorTime,Long cursorId,int limit);

    public String markAllAsSeen(Set<Long> notificationIDs, Authentication authentication);
    public List<RoleNotification> getNotificationsByStatus(String status,LocalDateTime cursorTime,Long cursorId,int limit,Authentication authentication);
}
