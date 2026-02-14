package com.quinzex.service;

import com.quinzex.dto.RoleNotificationDTO;
import com.quinzex.entity.RoleNotification;

public interface IRoleNotificationProducer {

    public void publish(RoleNotificationDTO roleNotification);
}
