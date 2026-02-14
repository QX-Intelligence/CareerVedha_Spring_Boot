package com.quinzex.service;

import com.quinzex.dto.PostNotificationDto;
import com.quinzex.entity.PostNotification;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface IPostNotificationService {
    public void postNotification(PostNotificationDto postNotificationDto);
    public List<PostNotification> getPostNotifications(Authentication authentication, LocalDateTime createdAt, Long cursorId, int size);
    public void markNotificationAsSeen(Long notificationId);
}
