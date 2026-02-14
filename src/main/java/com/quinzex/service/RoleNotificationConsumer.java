package com.quinzex.service;

import com.quinzex.config.KafkaConfig;
import com.quinzex.dto.RoleNotificationDTO;
import com.quinzex.entity.RoleNotification;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RoleNotificationConsumer implements IRoleNotificationConsumer {

    private final INotificationService notificationService;

    public RoleNotificationConsumer(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    @KafkaListener(topics = KafkaConfig.ROLE_NOTIFICATION_TOPIC,groupId = "role-notification-group")
    public void consume(RoleNotificationDTO roleNotification) {
       notificationService.sendNotification(roleNotification.getEmail(),roleNotification.getRole(),roleNotification.getMessage());
    }
}
