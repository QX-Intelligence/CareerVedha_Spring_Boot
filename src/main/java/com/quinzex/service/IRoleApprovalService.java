package com.quinzex.service;

import org.springframework.security.core.Authentication;

public interface IRoleApprovalService {

    public String approve (Long notificationId, Authentication authentication);
    public   String reject(Long notificationId, String reason, Authentication authentication);
}
