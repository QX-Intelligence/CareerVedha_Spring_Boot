package com.quinzex.service;

import com.quinzex.config.KafkaConfig;
import com.quinzex.dto.RoleNotificationDTO;
import com.quinzex.entity.RoleNotification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RoleNotificationProducer implements IRoleNotificationProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public RoleNotificationProducer(KafkaTemplate<String,Object>  kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }
    @Override
    public void publish(RoleNotificationDTO roleNotification) {
        kafkaTemplate.send(
                KafkaConfig.ROLE_NOTIFICATION_TOPIC,
                roleNotification.getRole(),
                roleNotification
        );
    }
}
