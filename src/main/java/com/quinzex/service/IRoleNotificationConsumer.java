package com.quinzex.service;

import com.quinzex.dto.RoleNotificationDTO;
import com.quinzex.entity.RoleNotification;

public interface IRoleNotificationConsumer {

    public void consume(RoleNotificationDTO roleNotification);
}
