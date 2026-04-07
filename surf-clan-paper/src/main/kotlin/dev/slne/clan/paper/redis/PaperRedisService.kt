package dev.slne.clan.paper.redis

import com.google.auto.service.AutoService
import dev.slne.surf.clan.core.client.redis.RedisService

@AutoService(RedisService::class)
class PaperRedisService : RedisService()