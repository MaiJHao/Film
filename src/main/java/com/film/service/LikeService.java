package com.film.service;

import com.film.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    // 点赞
    public void like(int userId, int entityType, int entityId, int entityUserId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
        boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if (isMember) {
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
            redisTemplate.opsForValue().decrement(userLikeKey);
        } else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
            redisTemplate.opsForValue().increment(userLikeKey);
        }
    }

    // 获取实体赞数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        long count = redisTemplate.opsForSet().size(entityLikeKey);
        return count;
    }

    // 获取某用户对实体的点赞状态
    public int findUserToEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        int status = redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
        return status;
    }

    // 获取某个用户获得的赞
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count;
    }
}
