package com.quinzex.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
public static final String ROLE_NOTIFICATION_TOPIC = "role-notifications";
public static final String POST_NOTIFICATION_TOPIC = "post-notifications";
    @Bean
    public NewTopic roleNotificationTopic(){
        short replicationFactor = 1;
        return new NewTopic(ROLE_NOTIFICATION_TOPIC,3,replicationFactor);

    }
    @Bean
    public NewTopic postNotificationTopic(){
        short replicationFactor = 1;
        return new NewTopic(POST_NOTIFICATION_TOPIC,3,replicationFactor);
    }
}
