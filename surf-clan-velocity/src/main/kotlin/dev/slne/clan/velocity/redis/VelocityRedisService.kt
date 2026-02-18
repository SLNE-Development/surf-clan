package dev.slne.clan.velocity.redis

import com.google.auto.service.AutoService
import dev.slne.clan.core.redis.RedisService

@AutoService(RedisService::class)
class VelocityRedisService : RedisService()