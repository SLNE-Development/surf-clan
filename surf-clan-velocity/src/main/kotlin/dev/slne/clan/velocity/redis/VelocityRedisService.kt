package dev.slne.clan.velocity.redis

import com.google.auto.service.AutoService
import dev.slne.surf.clan.core.client.redis.RedisService

@AutoService(RedisService::class)
class VelocityRedisService : RedisService()