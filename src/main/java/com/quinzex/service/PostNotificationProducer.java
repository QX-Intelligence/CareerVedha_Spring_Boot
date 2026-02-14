package com.quinzex.service;

import com.quinzex.config.KafkaConfig;

import com.quinzex.dto.PostNotificationDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PostNotificationProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public PostNotificationProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendNotification(PostNotificationDto postNotificationDto) {
        kafkaTemplate.send(KafkaConfig.POST_NOTIFICATION_TOPIC, postNotificationDto.getReceiverRole(), postNotificationDto);
    }

}
