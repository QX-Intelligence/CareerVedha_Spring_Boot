package com.quinzex.service;

import com.quinzex.config.KafkaConfig;
import com.quinzex.dto.PostNotificationDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PostNotificationConsumer {
   private final IPostNotificationService postNotificationService;
   private final PostNotificationCounterService postNotificationCounterService;
   public PostNotificationConsumer(IPostNotificationService postNotificationService, PostNotificationCounterService postNotificationCounterService) {
       this.postNotificationService = postNotificationService;
       this.postNotificationCounterService = postNotificationCounterService;
   }

    @KafkaListener(
            topics = KafkaConfig.POST_NOTIFICATION_TOPIC,
            groupId = "post-notification-group"
    )
    public void consume(PostNotificationDto postNotificationDto) {
        postNotificationService.postNotification(postNotificationDto);
        postNotificationCounterService.incrementForRole(postNotificationDto.getReceiverRole().toUpperCase());
        postNotificationCounterService.incrementForAdmin();
        postNotificationCounterService.incrementForSuperAdmin();
    }

}
