package com.quinzex.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PostNotificationCounterService {
private static final String PREFIX = "notification:unseen:role:";
    private static final String ADMIN_KEY = "notification:unseen:admin";
    private static final String SUPER_ADMIN_KEY  = "notification:unseen:super-admin";

private final StringRedisTemplate stringRedisTemplate;

public PostNotificationCounterService(StringRedisTemplate stringRedisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
}
public void incrementForRole(String role) {
    stringRedisTemplate.opsForValue().increment(PREFIX + role);
}
    public void incrementForSuperAdmin() {
        stringRedisTemplate.opsForValue().increment(SUPER_ADMIN_KEY );
    }
    public void incrementForAdmin() {
        stringRedisTemplate.opsForValue().increment(ADMIN_KEY);
    }

public long getCounterForRole(String role) {
    String value = stringRedisTemplate.opsForValue().get(PREFIX + role);
    return value == null ? 0 : Long.parseLong(value);
}

public long getCounterForSuperAdmin() {
    String value = stringRedisTemplate.opsForValue().get(SUPER_ADMIN_KEY );
    return value == null ? 0 : Long.parseLong(value);
}
public long getCounterForAdmin() {
    String value = stringRedisTemplate.opsForValue().get(ADMIN_KEY);
    return value == null ? 0 : Long.parseLong(value);
}
public void resetForRole(String role) {
    stringRedisTemplate.opsForValue().set(PREFIX+role,"0");
}
public void resetForSuperAdmin() {
    stringRedisTemplate.opsForValue().set(SUPER_ADMIN_KEY ,"0");
}
public void resetForAdmin() {
    stringRedisTemplate.opsForValue().set(ADMIN_KEY,"0");
}
}
