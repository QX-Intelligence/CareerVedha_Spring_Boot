package com.quinzex.dto;

import lombok.Data;

@Data
public class PostNotificationDto {
    private Long postId;
    private String receiverRole;
    private String message;
}
